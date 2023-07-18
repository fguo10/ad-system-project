package com.example.adsponsor.service.mapper;

import com.example.adcommon.dump.table.AdCreativeTable;
import com.example.adsponsor.entity.Creative;


// mapper: AdCreative - AdCreativeTable
public class AdCreativeMapper {
    public static AdCreativeTable toTable(Creative creative){
        return new AdCreativeTable(
                creative.getId(),
                creative.getName(),
                creative.getType(),
                creative.getMaterialType(),
                creative.getHeight(),
                creative.getWidth(),
                creative.getAuditStatus(),
                creative.getUrl()
        );
    }
}
