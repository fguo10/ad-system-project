package com.example.adsearch.index;

import lombok.Getter;

/**
 * 常用状态枚举类
 */
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