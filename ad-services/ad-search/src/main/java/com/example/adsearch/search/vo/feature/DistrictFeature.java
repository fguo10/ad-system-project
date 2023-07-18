package com.example.adsponsor.search.vo.feature;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistrictFeature{
    private List<StateAndCity> districts;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StateAndCity {
        private String state;
        private String city;
    }
}
