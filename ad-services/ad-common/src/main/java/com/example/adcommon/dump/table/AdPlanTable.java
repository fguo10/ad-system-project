package com.example.adcommon.dump.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import com.alibaba.fastjson.JSON;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdPlanTable {
    private Long planId;
    private Long userId;
    private Integer planStatus;
    private Date startDate;
    private Date endDate;

    public static AdPlanTable parseFromString(String data) {
        return JSON.parseObject(data, AdPlanTable.class);
    }
}
