package com.example.adsponsor.listeners;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class SharedEntityListener {
    @PrePersist
    public void prePersist(Object entity) throws NoSuchFieldException, IllegalAccessException {
        Field createTimeField = entity.getClass().getDeclaredField("createTime");
        Field updateTimeField = entity.getClass().getDeclaredField("updateTime");
        LocalDateTime now = LocalDateTime.now();
        createTimeField.setAccessible(true);
        updateTimeField.setAccessible(true);
        createTimeField.set(entity, now);
        updateTimeField.set(entity, now);
    }

    @PreUpdate
    public void preUpdate(Object entity) throws NoSuchFieldException, IllegalAccessException {
        Field updateTimeField = entity.getClass().getDeclaredField("updateTime");
        LocalDateTime now = LocalDateTime.now();
        updateTimeField.setAccessible(true);
        updateTimeField.set(entity, now);
    }
}
