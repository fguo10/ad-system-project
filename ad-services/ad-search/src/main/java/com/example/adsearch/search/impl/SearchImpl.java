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
    /**
     * 核心函数: 执行广告检索，获取广告创意响应结果
     *
     * @param request 广告检索请求对象
     * @return 广告检索响应对象
     */
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


            // 根据筛选的AdUnitIds获取AdUnitObjects对象,并筛选出valid的AdUnitObjects对象
            List<AdUnitObject> unitObjects = DataTable.of(AdUnitIndex.class).fetch(targetUnitIdSet);
            filterAdUnitAndPlanStatus(unitObjects, CommonStatus.VALID);

            // 根据AdUnitObjects获取CreativeIds，并最终获得CreativeObjects，这是最终响应需要的内容
            List<Long> CreativeIds = DataTable.of(CreativeUnitIndex.class).getCreativeByUnitObjects(unitObjects);
            List<CreativeObject> creativeObjects = DataTable.of(CreativeIndex.class).fetch(CreativeIds);

            //再再过滤： 根据广告位条件过滤创意对象
            filterCreativeByAdSlot(creativeObjects, adSlot.getWidth(), adSlot.getHeight(), adSlot.getType());

            // 构建指定广告位对应随机的符合条件的creativeObject/
            adSlot2Ads.put(adSlot.getAdSlotCode(), buildCreativeResponse(creativeObjects));
        }

        log.info("build response successfully, request={}, response={}", request.toString(), response.toString());
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

    /**
     * 根据过滤广告单元和广告计划状态过滤AdUnit对象
     *
     * @param unitObjects 广告单元对象列表
     * @param status      广告单元和计划的状态
     */
    private void filterAdUnitAndPlanStatus(List<AdUnitObject> unitObjects, CommonStatus status) {

        if (CollectionUtils.isEmpty(unitObjects)) return;

        CollectionUtils.filter(unitObjects, object -> object.getUnitStatus().equals(status.getStatus()) && object.getAdPlanObject().getPlanStatus().equals(status.getStatus()));
    }


    /**
     * 根据广告位条件过滤创意对象
     *
     * @param creativeObjects 创意对象列表
     * @param width           广告位宽度
     * @param height          广告位高度
     * @param type            广告位类型列表
     */
    private void filterCreativeByAdSlot(List<CreativeObject> creativeObjects, Integer width, Integer height, List<Integer> type) {

        if (CollectionUtils.isEmpty(creativeObjects)) return;

        CollectionUtils.filter(creativeObjects, creative ->
                creative.getAuditStatus().equals(CommonStatus.VALID.getStatus())
                        && creative.getWidth().equals(width)
                        && creative.getHeight().equals(height)
                        && type.contains(creative.getType())
        );
    }


    /**
     * 构建SearchResponse
     *
     * @param creativeObjects 创意对象列表
     * @return 创意响应列表
     */
    private List<SearchResponse.Creative> buildCreativeResponse(List<CreativeObject> creativeObjects) {

        if (CollectionUtils.isEmpty(creativeObjects)) return Collections.emptyList();

        // 列表中随机选择一个创意对象作为响应结果
        CreativeObject randomObject = creativeObjects.get(Math.abs(new Random().nextInt()) % creativeObjects.size());
        return Collections.singletonList(SearchResponse.convert(randomObject));
    }

}
