package com.example.adsponsor.search.vo.media;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Device {

    // 设备id
    private String deviceCode;
    // mac地址
    private String mac;
    // ip地址
    private String ip;
    // 设备型号编码
    private String model;
    // 分辨率尺寸
    private String displaySize;
    // 屏幕尺寸
    private String screenSize;
    // 设备序列号
    private String serialName;
}
