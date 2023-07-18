package com.example.adsearch.index.creative;

import com.example.adsearch.index.IndexAware;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class CreativeIndex implements IndexAware<Long, CreativeObject> {
    // 正向索引
    private static final Map<Long, CreativeObject> objectMap;

    // 线程安全的Map
    static {
        objectMap = new ConcurrentHashMap<>();
    }

    @Override
    public CreativeObject get(Long key) {
        return objectMap.get(key);
    }

    @Override
    public void add(Long key, CreativeObject val) {
        log.info("before add: {}", objectMap);
        objectMap.put(key, val);
        log.info("after add: {}", objectMap);
    }

    @Override
    public void update(Long key, CreativeObject val) {
        log.info("before update: {}", objectMap);

        CreativeObject oldObject = objectMap.get(key);
        if (oldObject == null) {
            objectMap.put(key, val);
        } else {
            oldObject.update(val);
        }

        log.info("after update: {}", objectMap);
    }

    @Override
    public void delete(Long key, CreativeObject val) {
        log.info("before delete: {}", objectMap);
        objectMap.remove(key);
        log.info("after delete: {}", objectMap);
    }


    /**
     * 根据创意ID列表获取创意对象列表
     *
     * @param creativeIds 创意ID列表
     * @return 创意对象列表
     */
    public List<CreativeObject> fetch(Collection<Long> creativeIds) {
        if (CollectionUtils.isEmpty(creativeIds)) return Collections.emptyList();

        List<CreativeObject> creativeObjects = new ArrayList<>();

        for (Long creativeId : creativeIds) {
            CreativeObject creativeObject = get(creativeId);
            if (creativeObject != null) creativeObjects.add(creativeObject);
        }
        return creativeObjects;
    }
}
