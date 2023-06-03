package com.example.adsponsor.constant;

import lombok.Getter;
import lombok.Setter;


@Getter
public enum CommonStatus {

    VALID(1, "VALID"),
    INVALID(0, "UNVALID");

    private final Integer status;
    private final String desc;

    CommonStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }
}
