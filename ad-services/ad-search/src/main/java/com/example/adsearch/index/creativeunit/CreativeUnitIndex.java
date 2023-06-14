package com.example.adsearch.index.creativeunit;


import com.example.adsearch.index.IndexAware;
import com.example.adsearch.index.adunit.AdUnitObject;
import com.example.adsearch.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Component
public class CreativeUnitIndex implements IndexAware<String, CreativeUnitObject> {
    // 正向索引 <adId-unitId, CreativeUnitObject>
    private static Map<String, CreativeUnitObject> objectMap;
    // 倒排索引 <adId, unitId Set>
    private static Map<Long, Set<Long>> creativeUnitMap;
    // 倒排索引 <unitId, adId set>
    private static Map<Long, Set<Long>> unitCreativeMap;


    static {
        objectMap = new ConcurrentHashMap<>();
        creativeUnitMap = new ConcurrentHashMap<>();
        unitCreativeMap = new ConcurrentHashMap<>();
    }

    @Override
    public CreativeUnitObject get(String key) {
        return objectMap.get(key);
    }

    @Override
    public void add(String key, CreativeUnitObject val) {
        log.info("before add: {}", objectMap);

        objectMap.put(key, val);

        Set<Long> unitSet = creativeUnitMap.get(val.getAdId());
        if (CollectionUtils.isEmpty(unitSet)) {
            unitSet = new ConcurrentSkipListSet<>();
            creativeUnitMap.put(val.getAdId(), unitSet);
        }
        unitSet.add(val.getUnitId());

        Set<Long> creativeSet = unitCreativeMap.get(val.getUnitId());
        if (CollectionUtils.isEmpty(creativeSet)) {
            creativeSet = new ConcurrentSkipListSet<>();
            unitCreativeMap.put(val.getUnitId(), creativeSet);
        }
        creativeSet.add(val.getAdId());

        log.info("after add: {}", objectMap);
    }

    @Override
    public void update(String key, CreativeUnitObject val) {
        log.error("CreativeUnitIndex not support update");
    }

    @Override
    public void delete(String key, CreativeUnitObject val) {
        log.info("before delete: {}", objectMap);

        objectMap.remove(key);

        Set<Long> unitSet = creativeUnitMap.get(val.getAdId());
        if (CollectionUtils.isNotEmpty(unitSet)) {
            unitSet.remove(val.getUnitId());
        }

        Set<Long> creativeSet = unitCreativeMap.get(val.getUnitId());
        if (CollectionUtils.isNotEmpty(creativeSet)) {
            creativeSet.remove(val.getAdId());
        }

        log.info("after delete: {}", objectMap);
    }

    public List<Long> selectAds(List<AdUnitObject> unitObjects) {

        if (CollectionUtils.isEmpty(unitObjects)) {
            return Collections.emptyList();
        }

        List<Long> result = new ArrayList<>();

        for (AdUnitObject unitObject : unitObjects) {

            Set<Long> adIds = unitCreativeMap.get(unitObject.getUnitId());
            if (CollectionUtils.isNotEmpty(adIds)) {
                result.addAll(adIds);
            }
        }

        return result;
    }
}
