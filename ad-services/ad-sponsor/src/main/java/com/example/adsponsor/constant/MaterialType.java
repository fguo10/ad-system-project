package com.example.adsponsor.constant;

public enum MaterialType {
    PNG(1, "png"),
    JPG(2, "jpg"),
    MP4(3, "mp4"),
    MOV(4, "mov");


    private final Integer status;
    private final String desc;

    MaterialType(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

}

