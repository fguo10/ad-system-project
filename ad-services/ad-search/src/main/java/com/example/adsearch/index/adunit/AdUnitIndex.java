package com.example.adsearch.index.adunit;

import com.example.adsearch.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 正向索引: 实现AdUnit索引的增删改查
@Slf4j
@Component
public class AdUnitIndex implements IndexAware<Long, AdUnitObject> {

    private static final Map<Long, AdUnitObject> objectMap;

    // 使用线程安全的Map: when operate the map, it needs thread safe.
    static {
        objectMap = new ConcurrentHashMap<>();
    }

    @Override
    public AdUnitObject get(Long key) {
        return objectMap.get(key);
    }

    @Override
    public void add(Long key, AdUnitObject val) {
        log.info("before add: {}", objectMap);
        objectMap.put(key, val);
        log.info("after add: {}", objectMap);
    }

    @Override
    public void update(Long key, AdUnitObject val) {
        log.info("before update: {}", objectMap);
        AdUnitObject oldAdUnitObject = objectMap.get(key);
        if (oldAdUnitObject == null) {
            objectMap.put(key, val);
        } else {
            oldAdUnitObject.update(val);
        }
        log.info("after update: {}", objectMap);

    }

    @Override
    public void delete(Long key, AdUnitObject val) {
        log.info("before delete: {}", objectMap);
        objectMap.remove(key);
        log.info("after delete: {}", objectMap);

    }
}
