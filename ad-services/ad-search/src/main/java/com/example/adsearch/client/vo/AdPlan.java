package com.example.adsearch.client.vo;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 广告计划实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdPlan {
    private Long id;
    private Long userId;
    private String planName;
    private Integer planStatus;
    private Date startDate;
    private Date endDate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
