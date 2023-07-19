package com.example.adsearch.search.vo.media;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdSlot {
    //广告位编码
    private String adSlotCode;
    // 广告位类型
    private Integer positionType;
    // 广告位宽度和高度
    private Integer width;
    private Integer height;
    // 广告物料的类型，可支持多种, eg:图片,视频
    private List<Integer> type;
    // 广告的最低出价
    private Integer minCpm;
}
