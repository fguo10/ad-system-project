package com.example.adsearch.client.vo;

import lombok.*;

import java.util.List;

/**
 * 广告计划获取请求类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlanGetRequest {
    private Long userId;
    private List<Long> ids;
}
