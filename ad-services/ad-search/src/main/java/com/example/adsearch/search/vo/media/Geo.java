package com.example.adsearch.search.vo.media;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Geo {
    // 经纬度
    private Float latitude;
    private Float longitude;

    // 州和城市
    private String city;
    private String state;
}
