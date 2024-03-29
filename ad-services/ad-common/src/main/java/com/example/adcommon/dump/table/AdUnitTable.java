package com.example.adcommon.dump.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdUnitTable {
    private Long unitId;
    private Integer unitStatus;
    private Integer positionType;
    private Long planId;
}
