package com.example.adsponsor.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdPlanSearchRequest {
    private Long userId;
    private List<Long> adPlanIds;
}
