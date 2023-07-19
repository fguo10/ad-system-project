package com.example.adsearch.handler.mapper;

import com.example.adcommon.dump.table.AdCreativeTable;
import com.example.adcommon.dump.table.AdPlanTable;
import com.example.adsearch.index.adplan.AdPlanObject;
import com.example.adsearch.index.creative.CreativeObject;

public class AdCreativeMapper {
    public static CreativeObject toObject(AdCreativeTable creativeTable) {
        return new CreativeObject(creativeTable.getAdId(), creativeTable.getName(), creativeTable.getType(), creativeTable.getMaterialType(), creativeTable.getHeight(), creativeTable.getWidth(), creativeTable.getAuditStatus(), creativeTable.getAdUrl());
    }
}
