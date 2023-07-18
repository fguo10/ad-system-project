package com.example.adsearch.index.adunit;

import com.example.adsearch.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
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


    // 获取符合条件的推广单元ids
    public Set<Long> match(Integer positionType) {
        Set<Long> adUnitIds = new HashSet<>();
        objectMap.forEach((k, v) -> {
            if (AdUnitObject.isAdSoltTypeOk(positionType, v.getPositionType())) {
                adUnitIds.add(k);
            }
        });
        return adUnitIds;
    }

    // 根据推广单元的ids获取推广单元的对象
    public List<AdUnitObject> fetch(Collection<Long> adUnitIds) {
        if (CollectionUtils.isEmpty(adUnitIds)) return Collections.emptyList();

        List<AdUnitObject> result = new ArrayList<>();
        adUnitIds.forEach(u -> {
            AdUnitObject unitObject = get(u);
            if (null == unitObject) {
                log.error("AdUnitObject not found: {}", u);
            }
            result.add(unitObject);
        });
        return result;
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
