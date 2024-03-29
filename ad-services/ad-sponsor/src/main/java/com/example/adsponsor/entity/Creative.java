package com.example.adsponsor.entity;

import com.example.adsponsor.constant.AuditStatus;
import com.example.adsponsor.listeners.SharedEntityListener;
import jakarta.persistence.*;
import lombok.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;

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

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;


    /*
    {
        "name":"",
        "type":,
        "materialType":,
        "height":,
        "width":,
        "size":
        "userId":,
        "url":""
    }


     */
    public Creative(String name, Integer type, Integer materialType, Integer height, Integer width, Long size, Long userId, String url) {
        this.name = name;
        this.type = type;
        this.materialType = materialType;
        this.height = height;
        this.width = width;
        this.size = size;
        this.auditStatus = AuditStatus.NOT_AUDIT.getStatus();
        this.userId = userId;
        this.url = url;
    }
}
