package com.example.adsponsor.entity;

import com.example.adsponsor.listeners.SharedEntityListener;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(SharedEntityListener.class)
@Table(name = "ad_unit")
public class AdUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "plan_id", nullable = false)
    private Long planId;

    @Column(name = "unit_name", nullable = false)
    private String unitName;

    @Column(name = "unit_status", nullable = false)
    private Integer unitStatus;

    /**
     * 广告位类型(开屏, 贴片, 中贴...)
     */
    @Column(name = "position_type", nullable = false)
    private Integer positionType;

    @Column(name = "budget", nullable = false)
    private Long budget;

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    public AdUnit(Long planId, String unitName, Integer unitStatus, Integer positionType, Long budget) {
        this.planId = planId;
        this.unitName = unitName;
        this.unitStatus = unitStatus;
        this.positionType = positionType;
        this.budget = budget;
    }
}
