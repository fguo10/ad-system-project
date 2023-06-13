package com.example.adsearch.index.keyword;

import com.example.adsearch.index.IndexAware;
import com.example.adsearch.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;


@Slf4j
@Component
public class UnitKeywordIndex implements IndexAware<String, Set<Long>> {

    // 使用倒排索引
    private static final Map<String, Set<Long>> keywordUnitMap;
    // 使用正向索引(一个adunit可能对应多个keyword)
    private static final Map<Long, Set<String>> unitKeywordMap;


    // thread safe, so we use concurrentHashMap
    static {
        keywordUnitMap = new ConcurrentHashMap<>();
        unitKeywordMap = new ConcurrentHashMap<>();

    }

    @Override
    public Set<Long> get(String key) {
        // StringUtils.isEmpty is deprecated.
        if (Objects.isNull(key) || key.isEmpty()) return Collections.emptySet();

        // simplify version
        return keywordUnitMap.getOrDefault(key, Collections.emptySet());
    }

    @Override
    public void add(String key, Set<Long> val) {

        log.info("UnitKeywordIndex, before add: {}", unitKeywordMap);

        // ConcurrentSkipListSet: thread-safe, implement SortedSet
        Set<Long> unitIdSet = CommonUtils.getorCreate(key, keywordUnitMap, ConcurrentSkipListSet::new);
        unitIdSet.addAll(val);

        for (Long unitId : val) {
            Set<String> keywordSet = CommonUtils.getorCreate(unitId, unitKeywordMap, ConcurrentSkipListSet::new);
            keywordSet.add(key);
        }

        log.info("UnitKeywordIndex, after add: {}", unitKeywordMap);
    }

    @Override
    public void update(String key, Set<Long> val) {
        log.error("keyword index can not support update");
    }

    @Override
    public void delete(String key, Set<Long> val) {
        log.info("UnitKeywordIndex, before delete: {}", unitKeywordMap);

        Set<Long> unitIds = CommonUtils.getorCreate(key, keywordUnitMap, ConcurrentSkipListSet::new);
        unitIds.removeAll(val);

        for (Long unitId : val) {
            Set<String> keywordSet = CommonUtils.getorCreate(unitId, unitKeywordMap, ConcurrentSkipListSet::new);
            keywordSet.remove(key);
        }

        log.info("UnitKeywordIndex, after delete: {}", unitKeywordMap);

    }

    public boolean match(Long unitId, List<String> keywords) {
        Set<String> unitKeywords = unitKeywordMap.get(unitId);
        if (CollectionUtils.isNotEmpty(unitKeywords)) {
            // 检查unitKeywords是否包含了keywords中的所有元素? true:false
//            return CollectionUtils.isSubCollection(keywords, unitKeywords);
            return unitKeywords.containsAll(keywords);
        }
        return false;
    }
}
