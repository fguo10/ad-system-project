package com.example.adsearch.search.vo;

import com.example.adsearch.index.creative.CreativeObject;
import lombok.*;

import java.util.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {
    private Map<String, List<Creative>> adSlot2Ads = new HashMap<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Creative {
        // 广告id
        private Long adId;
        // 广告url
        private String adUrl;
        // 广告宽度和高度
        private Integer width;
        private Integer height;
        // 广告类型,eg:图片，视频
        private Integer type;
        // 物料的类型,eg:jpg, png....
        private Integer materialType;

        // 展示监测URL列表, 通常用于跟踪广告展示的情况，以便进行监测和统计
        private List<String> showMonitorUrl = Arrays.asList("www.example.com", "www.example.com");

        // 点击监测URL列表, L用于跟踪广告被点击的情况，以便进行监测和统计。
        private List<String> clickMonitorUrl = Arrays.asList("www.example.com", "www.example.com");

    }

    // 转换CreativeObject为Creative对象。
    public static Creative convert(CreativeObject object) {
        Creative creative = new Creative();
        creative.setAdId(object.getAdId());
        creative.setAdUrl(object.getAdUrl());
        creative.setWidth(object.getWidth());
        creative.setHeight(object.getHeight());
        creative.setType(object.getType());
        creative.setMaterialType(object.getMaterialType());

        return creative;

    }
}
