package com.example.adsearch.handler.mapper;

import com.example.adcommon.dump.table.AdPlanTable;
import com.example.adsearch.index.adplan.AdPlanObject;

// mapper: Table & Object
public class AdPlanMapper {
    public static AdPlanObject toObject(AdPlanTable planTable) {
        return new AdPlanObject(planTable.getPlanId(), planTable.getUserId(), planTable.getPlanStatus(), planTable.getStartDate(), planTable.getEndDate());
    }
}
