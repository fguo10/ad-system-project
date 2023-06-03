package com.example.adsponsor.listeners;

import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

@Slf4j
public class SharedEntityListener {
    @PrePersist
    public void prePersist(Object entity) throws NoSuchFieldException, IllegalAccessException {

        log.info("===== " + String.valueOf(entity.getClass()));
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
        log.info("===== " + String.valueOf(entity.getClass()));
        Field updateTimeField = entity.getClass().getDeclaredField("updateTime");
        LocalDateTime now = LocalDateTime.now();
        updateTimeField.setAccessible(true);
        updateTimeField.set(entity, now);
    }
}
