package com.example.adsearch.search.impl;

import com.example.adsearch.index.DataTable;
import com.example.adsearch.index.adunit.*;
import com.example.adsearch.index.creative.*;
import com.example.adsearch.index.creativeunit.CreativeUnitIndex;
import com.example.adsearch.index.district.UnitDistrictIndex;
import com.example.adsearch.index.interest.UnitInterestIndex;
import com.example.adsearch.index.keyword.UnitKeywordIndex;
import com.example.adsearch.search.SearchInterface;
import com.example.adsearch.search.vo.*;
import com.example.adsearch.search.vo.feature.*;
import com.example.adsearch.search.vo.media.*;
import com.example.adsponsor.constant.CommonStatus;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/*
    该类核心功能是: 根据请求获取广告，并根据一些特征进行筛选和过滤。
    - fallback 方法用于回退处理，在发生异常时返回一个空的 SearchResponse 对象。
    - fetchAds 方法通过一系列的过滤和筛选操作，将符合条件的广告添加到 adSlot2Ads 中，并返回一个 SearchResponse 对象。
        - getORRelationUnitIds、filterKeywordFeature、filterDistrictFeature 等，用于进行特征的过滤和筛选操作。
        - buildCreativeResponse 方法用于构建广告的响应数据。

 */
@Slf4j
@Service
public class SearchImpl implements SearchInterface {
    @Override
    public SearchResponse fetchAds(SearchRequest request) {

        // 1. 解析广告检索服务的请求信息
        // 1-1. 获取请求的广告位信息
        List<AdSlot> adSlots = request.getRequestInfo().getAdSlots();
        // 1-2. 获取3个feature和关联关系(and/or): 关键字，地域，兴趣
        KeywordFeature keywordFeature = request.getFeatureInfo().getKeywordFeature();
        DistrictFeature districtFeature = request.getFeatureInfo().getDistrictFeature();
        InterestFeature interestFeature = request.getFeatureInfo().getInterestFeature();
        FeatureRelation relation = request.getFeatureInfo().getRelation();

        // 2. 构建广告检索服务的响应信息
        // 2-1. 初始化响应信息,默认每个广告位包括一系列广告创意(key: adSlot, val: list of Creatives)
        SearchResponse response = new SearchResponse();
        Map<String, List<SearchResponse.Creative>> adSlot2Ads = response.getAdSlot2Ads();

        // 2-2. 遍历所有的广告位，生成创意列表数据
        for (AdSlot adSlot : adSlots) {
            Set<Long> targetUnitIdSet;

            // 预过滤: 根据positionType获取初始的AdUnit
            Set<Long> adUnitIdSet = DataTable.of(AdUnitIndex.class).match(adSlot.getPositionType());

            // 再过滤: 根据3个feature信息和关联关系对AdUnit再次过滤，缩小范围
            if (relation == FeatureRelation.AND) {
                filterKeywordFeature(adUnitIdSet, keywordFeature);
                filterDistrictFeature(adUnitIdSet, districtFeature);
                filterInterestTagFeature(adUnitIdSet, interestFeature);

                targetUnitIdSet = adUnitIdSet;
            } else {
                targetUnitIdSet = getORRelationUnitIds(adUnitIdSet, keywordFeature, districtFeature, itFeature);
            }


            //
            List<AdUnitObject> unitObjects = DataTable.of(AdUnitIndex.class).fetch(targetUnitIdSet);
            filterAdUnitAndPlanStatus(unitObjects, CommonStatus.VALID);

            List<Long> adIds = DataTable.of(CreativeUnitIndex.class).selectAds(unitObjects);
            List<CreativeObject> creativeObjects = DataTable.of(CreativeIndex.class).fetch(adIds);

            filterCreativeByAdSlot(creativeObjects, adSlot.getWidth(), adSlot.getHeight(), adSlot.getType());

            adSlot2Ads.put(adSlot.getAdSlotCode(), buildCreativeResponse(creativeObjects));
        }
        log.info("fetchAds: {}-{}", JSON.toJSONString(request), JSON.toJSONString(response));

        return response;

    }


    /**
     * 根据关键字特征对广告单元集合进行过滤
     *
     * @param adUnitIds      广告单元ID集合
     * @param keywordFeature 关键字特征对象
     */
    private void filterKeywordFeature(Collection<Long> adUnitIds, KeywordFeature keywordFeature) {
        if (CollectionUtils.isEmpty(adUnitIds)) return;

        if (CollectionUtils.isNotEmpty(keywordFeature.getKeywords())) {
            UnitKeywordIndex unitKeywordIndex = DataTable.of(UnitKeywordIndex.class);
            CollectionUtils.filter(adUnitIds, adUnitId -> unitKeywordIndex.match(adUnitId, keywordFeature.getKeywords()));
        }
    }


    /**
     * 根据地域特征对广告单元集合进行过滤
     *
     * @param adUnitIds       广告单元ID集合
     * @param districtFeature 地域特征对象
     */
    private void filterDistrictFeature(Collection<Long> adUnitIds, DistrictFeature districtFeature) {
        if (CollectionUtils.isEmpty(adUnitIds)) return;

        if (CollectionUtils.isNotEmpty(districtFeature.getDistricts())) {
            UnitDistrictIndex unitDistrictIndex = DataTable.of(UnitDistrictIndex.class);
            CollectionUtils.filter(adUnitIds, adUnitId -> unitDistrictIndex.match(adUnitId, districtFeature.getDistricts()));
        }
    }


    /**
     * 根据兴趣标签特征对广告单元集合进行过滤
     *
     * @param adUnitIds 广告单元ID集合
     * @param itFeature 兴趣标签特征对象
     */
    private void filterInterestTagFeature(Collection<Long> adUnitIds, InterestFeature itFeature) {

        if (CollectionUtils.isEmpty(adUnitIds)) return;


        if (CollectionUtils.isNotEmpty(itFeature.getInterests())) {
            UnitInterestIndex unitItIndex = DataTable.of(UnitInterestIndex.class);
            CollectionUtils.filter(adUnitIds, adUnitId -> unitItIndex.match(adUnitId, itFeature.getInterests()));
        }
    }


    /**
     * 获取按逻辑“或”关系合并的广告单元ID集合
     *
     * @param adUnitIdSet     广告单元ID集合
     * @param keywordFeature  关键词特征对象
     * @param districtFeature 地域特征对象
     * @param itFeature       兴趣标签特征对象
     * @return 合并后的广告单元ID集合
     */
    private Set<Long> getORRelationUnitIds(Set<Long> adUnitIdSet,
                                           KeywordFeature keywordFeature,
                                           DistrictFeature districtFeature,
                                           InterestFeature itFeature) {

        if (CollectionUtils.isEmpty(adUnitIdSet)) return Collections.emptySet();


        Set<Long> keywordUnitIdSet = new HashSet<>(adUnitIdSet);
        Set<Long> districtUnitIdSet = new HashSet<>(adUnitIdSet);
        Set<Long> itUnitIdSet = new HashSet<>(adUnitIdSet);

        filterKeywordFeature(keywordUnitIdSet, keywordFeature);
        filterDistrictFeature(districtUnitIdSet, districtFeature);
        filterInterestTagFeature(itUnitIdSet, itFeature);

        return new HashSet<>(CollectionUtils.union(CollectionUtils.union(keywordUnitIdSet, districtUnitIdSet), itUnitIdSet));
    }

}
