package com.example.adsponsor.constant;

import lombok.Getter;

@Getter
public enum CommonStatus {
    VALID(1),
    INVALID(0);

    private int value;

    CommonStatus(int value) {
        this.value = value;
    }

}
