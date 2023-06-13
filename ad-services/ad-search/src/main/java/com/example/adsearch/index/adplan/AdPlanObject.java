package com.example.adsearch.index.adplan;

import lombok.*;

import java.util.Date;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdPlanObject {
    private Long planId;
    private Long userId;
    private Integer planStatus;
    private Date startDate;
    private Date endDate;


    public void update(AdPlanObject object) {
        if (Objects.nonNull(object.getPlanId())) this.planId = object.getPlanId();
        if (Objects.nonNull(object.getUserId())) this.userId = object.getUserId();
        if (Objects.nonNull(object.getPlanStatus())) this.planStatus = object.getPlanStatus();
        if (Objects.nonNull(object.getStartDate())) this.startDate = object.getStartDate();
        if (Objects.nonNull(object.getEndDate())) this.endDate = object.getEndDate();
    }
}
