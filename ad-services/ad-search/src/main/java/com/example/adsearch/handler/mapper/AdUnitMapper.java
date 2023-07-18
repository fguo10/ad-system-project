package com.example.adsearch.handler.mapper;

import com.example.adcommon.dump.table.AdPlanTable;
import com.example.adcommon.dump.table.AdUnitTable;
import com.example.adsearch.index.adplan.AdPlanObject;
import com.example.adsearch.index.adunit.AdUnitObject;

// mapper: Table & Object
public class AdUnitMapper {
    public static AdUnitObject toObject(AdUnitTable unitTable, AdPlanObject adPlanObject) {
        return new AdUnitObject(unitTable.getUnitId(),
                unitTable.getUnitStatus(),
                unitTable.getPositionType(),
                unitTable.getPlanId(),
                adPlanObject
        );
    }
}
