package com.example.adsearch.index.interest;

import com.example.adsearch.index.IndexAware;
import com.example.adsearch.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Component
public class UnitItIndex implements IndexAware<String, Set<Long>> {

    // 倒排索引 <itTag, adUnitId set>
    private static final Map<String, Set<Long>> itUnitMap;

    // 正向索引 <unitId, itTag set>
    private static Map<Long, Set<String>> unitItMap;


    // thread safe
    static {
        itUnitMap = new ConcurrentHashMap<>();
        unitItMap = new ConcurrentHashMap<>();
    }

    @Override
    public Set<Long> get(String key) {
        return itUnitMap.get(key);
    }

    @Override
    public void add(String key, Set<Long> val) {

        log.info("UnitItIndex, before add: {}", unitItMap);

        // update 倒排索引
        Set<Long> unitIds = CommonUtils.getorCreate(key, itUnitMap, ConcurrentSkipListSet::new);
        unitIds.addAll(val);

        // update 正向索引
        for (Long unitId : val) {
            Set<String> its = CommonUtils.getorCreate(unitId, unitItMap, ConcurrentSkipListSet::new);
            its.add(key);
        }

        log.info("UnitItIndex, after add: {}", unitItMap);
    }

    @Override
    public void update(String key, Set<Long> val) {
        log.error("it index can not support update");
    }

    @Override
    public void delete(String key, Set<Long> val) {

        log.info("UnitItIndex, before delete: {}", unitItMap);

        // delete 倒排索引
        Set<Long> unitIds = CommonUtils.getorCreate(key, itUnitMap, ConcurrentSkipListSet::new);
        unitIds.removeAll(val);

        // delete 正向索引
        for (Long unitId : val) {
            Set<String> itTagSet = CommonUtils.getorCreate(unitId, unitItMap, ConcurrentSkipListSet::new);
            itTagSet.remove(key);
        }

        log.info("UnitItIndex, after delete: {}", unitItMap);

    }

    public boolean match(Long unitId, List<String> itTags) {
        Set<String> unitIts = unitItMap.get(unitId);

        if (CollectionUtils.isNotEmpty(unitIts)) {
            return CollectionUtils.isSubCollection(itTags, unitIts);
        }
        return false;
    }
}
