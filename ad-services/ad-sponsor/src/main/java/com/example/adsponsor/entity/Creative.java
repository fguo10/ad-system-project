package com.example.adsponsor.entity;

import com.example.adsponsor.constant.CommonStatus;
import com.example.adsponsor.listeners.SharedEntityListener;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(SharedEntityListener.class)
@Table(name = "ad_creative")
public class Creative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @Column(name = "name", nullable = false)
    private String name;


    @Column(name = "type", nullable = false)
    private Integer type;

    /**
     * 物料的类型, 比如图片可以是 bmp, jpg等等
     */

    @Column(name = "material_type", nullable = false)
    private Integer materialType;


    @Column(name = "height", nullable = false)
    private Integer height;


    @Column(name = "width", nullable = false)
    private Integer width;

    /**
     * 物料大小
     */

    @Column(name = "size", nullable = false)
    private Long size;

    /**
     * 持续时长, 只有视频不为0
     */

    @Column(name = "duration", nullable = false)
    private Integer duration;

    /**
     * 审核状态
     */

    @Column(name = "audit_status", nullable = false)
    private Integer auditStatus;


    @Column(name = "user_id", nullable = false)
    private Long userId;


    @Column(name = "url", nullable = false)
    private String url;

}
