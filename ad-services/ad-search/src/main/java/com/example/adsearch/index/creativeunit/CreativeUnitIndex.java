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
    // Creative和AdUnit是多对多的关系, 创建3个map，便于将来扩展使用
    // 正向索引: <creativeId-unitId, CreativeUnitObject>
    private static final Map<String, CreativeUnitObject> objectMap;

    // 倒排索引: <unitId, creativeId set>, 根据adUnitId获取创意id的集合
    private static final Map<Long, Set<Long>> unitCreativeMap;

    // 倒排索引: <creativeId, unitId Set>, 根据creativeId获取推广单元的id集合。
    private static final Map<Long, Set<Long>> creativeUnitMap;


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

        // 添加正向索引: <creativeId-unitId, CreativeUnitObject>
        objectMap.put(key, val);

        // 添加倒排索引: <creativeId, unitId Set>, 根据creativeId获取推广单元的id集合。
        Set<Long> unitIdsSet = creativeUnitMap.get(val.getCreativeId());
        if (CollectionUtils.isEmpty(unitIdsSet))
            creativeUnitMap.put(val.getCreativeId(), new ConcurrentSkipListSet<>());
        unitIdsSet.add(val.getUnitId());

        // 添加倒排索引: <unitId, creativeId set>, 根据adUnitId获取创意id的集合
        Set<Long> creativeIdsSet = unitCreativeMap.get(val.getUnitId());
        if (CollectionUtils.isEmpty(creativeIdsSet)) {
            unitCreativeMap.put(val.getUnitId(), new ConcurrentSkipListSet<>());
        }
        creativeIdsSet.add(val.getCreativeId());

        log.info("after add: {}", objectMap);
    }

    @Override
    public void update(String key, CreativeUnitObject val) {
        // 更新索引的成本非常高(涉及set的遍历)，因此不支持更新。建议: 先删除索引，再添加新的索引。
        log.error("CreativeUnitIndex not support update");
    }

    @Override
    public void delete(String key, CreativeUnitObject val) {
        log.info("before delete: {}", objectMap);

        // 删除正向索引: <creativeId-unitId, CreativeUnitObject>
        objectMap.remove(key);

        // 删除倒排索引: <creativeId, unitId Set>, 根据creativeId获取推广单元的id集合。
        Set<Long> unitIdsSet = creativeUnitMap.get(val.getCreativeId());
        if (CollectionUtils.isNotEmpty(unitIdsSet)) {
            unitIdsSet.remove(val.getUnitId());
        }

        // 删除倒排索引: <unitId, creativeId set>, 根据adUnitId获取创意id的集合
        Set<Long> creativeIdsSet = unitCreativeMap.get(val.getUnitId());
        if (CollectionUtils.isNotEmpty(creativeIdsSet)) {
            creativeIdsSet.remove(val.getCreativeId());
        }

        log.info("after delete: {}", objectMap);
    }
}
