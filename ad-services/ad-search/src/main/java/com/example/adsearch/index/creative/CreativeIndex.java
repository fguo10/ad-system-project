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
    private static Map<Long, CreativeObject> objectMap;

    static {
        objectMap = new ConcurrentHashMap<>();
    }

    public List<CreativeObject> fetch(Collection<Long> adIds) {

        if (CollectionUtils.isEmpty(adIds)) return Collections.emptyList();

        List<CreativeObject> result = new ArrayList<>();
        adIds.forEach(u -> {
            CreativeObject object = get(u);
            if (object == null) {
                log.error("CreativeObject not found: {}", u);
                return;
            }
            result.add(object);
        });

        return result;
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
}
