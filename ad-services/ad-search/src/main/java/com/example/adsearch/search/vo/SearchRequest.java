package com.example.adsponsor.search.vo;

import com.example.adsponsor.search.vo.feature.*;
import com.example.adsponsor.search.vo.media.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {

    // 媒体方的请求标识
    private String mediaId;
    // 请求基本信息
    private RequestInfo requestInfo;
    // 匹配信息
    private FeatureInfo featureInfo;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestInfo {
        // 唯一请求id
        private String requestId;
        // 广告位信息
        private List<AdSlot> adSlots;
        //
        private App app;
        private Geo geo;
        private Device device;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FeatureInfo {
        private KeywordFeature keywordFeature;
        private DistrictFeature districtFeature;
        private InterestFeature interestFeature;

        // 默认使用and的关系
        private FeatureRelation relation = FeatureRelation.AND;
    }
}
