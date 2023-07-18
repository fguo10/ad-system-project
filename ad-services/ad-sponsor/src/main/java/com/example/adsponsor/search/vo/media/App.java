package com.example.adsponsor.search.vo.media;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class App {
    // 应用编码
    private String appCode;
    // 应用名称
    private String appName;
    // 应用包名
    private String packageName;
    // 应用请求页面的名称
    private String activityName;
}
