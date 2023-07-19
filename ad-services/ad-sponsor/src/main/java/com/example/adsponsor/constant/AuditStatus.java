package com.example.adsponsor.constant;

import lombok.Getter;

@Getter
public enum AuditStatus {

    AUDIT(1, "audit"),
    NOT_AUDIT(0, "not audit");

    private final Integer status;
    private final String desc;

    AuditStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
