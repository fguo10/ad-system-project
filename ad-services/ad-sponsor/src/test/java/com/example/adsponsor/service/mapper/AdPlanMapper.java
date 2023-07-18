package com.example.adsponsor.service.mapper;

import com.example.adcommon.dump.table.AdPlanTable;
import com.example.adsponsor.entity.AdPlan;

// mapper: AdPlan - AdPlanTable
public class AdPlanMapper {
    public static AdPlanTable toTable(AdPlan adPlan){
        return new AdPlanTable(adPlan.getId(),
                adPlan.getUserId(),
                adPlan.getPlanStatus(),
                adPlan.getStartDate(),
                adPlan.getEndDate());
    }
}
