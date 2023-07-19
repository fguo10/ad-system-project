package com.example.adsponsor.service.mapper;

import com.example.adcommon.dump.table.AdUnitTable;
import com.example.adsponsor.entity.AdUnit;

// mapper: AdUnit - AdUnitTable
public class AdUnitMapper {
    public static AdUnitTable toTable(AdUnit adUnit){
        return new AdUnitTable(adUnit.getId(),
                adUnit.getUnitStatus(),
                adUnit.getPositionType(),
                adUnit.getPlanId());
    }
}
