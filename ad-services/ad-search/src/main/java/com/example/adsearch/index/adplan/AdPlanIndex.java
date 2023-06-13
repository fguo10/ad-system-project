package com.example.adsearch.index.adplan;

import com.example.adsearch.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 正向索引
@Slf4j
@Component
public class AdPlanIndex implements IndexAware<Long, AdPlanObject> {
    private static Map<Long, AdPlanObject> objectMap;

    // when operate the map, it needs thread safe.
    static {
        objectMap = new ConcurrentHashMap<>();
    }


    @Override
    public AdPlanObject get(Long key) {
        return objectMap.get(key);
    }

    @Override
    public void add(Long key, AdPlanObject val) {
        log.info("before add: {}", objectMap);
        objectMap.put(key, val);
        log.info("after add: {}", objectMap);
    }

    @Override
    public void update(Long key, AdPlanObject val) {
        log.info("before update: {}", objectMap);

        AdPlanObject oldObject = objectMap.get(key);
        if (oldObject == null) {
            objectMap.put(key, val);
        } else {
            oldObject.update(val);
        }

        log.info("after update: {}", objectMap);
    }

    @Override
    public void delete(Long key, AdPlanObject val) {
        log.info("before delete: {}", objectMap);
        objectMap.remove(key);
        log.info("after delete: {}", objectMap);
    }
}
