package com.example.adsearch.index.adunit;

import com.example.adsearch.index.adplan.AdPlanObject;
import lombok.*;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitObject {

    private Long unitId;
    private Integer unitStatus;
    private Integer positionType;
    private Long planId;

    private AdPlanObject adPlanObject;

    void update(AdUnitObject object) {
        if (Objects.nonNull(object.getPlanId())) this.unitId = object.getUnitId();
        if (Objects.nonNull(object.getUnitStatus())) this.unitStatus = object.getUnitStatus();
        if (Objects.nonNull(object.getPositionType())) this.positionType = object.getPositionType();
        if (Objects.nonNull(object.getPlanId())) this.planId = object.getPlanId();
        if (Objects.nonNull(object.getAdPlanObject())) this.adPlanObject = object.getAdPlanObject();
    }

}
