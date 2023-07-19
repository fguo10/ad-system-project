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
public class UnitInterestIndex implements IndexAware<String, Set<Long>> {

    // 倒排索引 <itTag, adUnitIds set>
    private static final Map<String, Set<Long>> itUnitMap;

    // 正向索引 <unitId, itTags set>
    private static final Map<Long, Set<String>> unitItMap;


    // 线程安全 thread safe
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

        // 添加倒排索引
        Set<Long> unitIds = CommonUtils.getOrCreate(key, itUnitMap, ConcurrentSkipListSet::new);
        unitIds.addAll(val);

        // 添加正向索引
        for (Long unitId : val) {
            Set<String> its = CommonUtils.getOrCreate(unitId, unitItMap, ConcurrentSkipListSet::new);
            its.add(key);
        }

        log.info("UnitItIndex, after add: {}", unitItMap);
    }

    @Override
    public void update(String key, Set<Long> val) {
        // 更新索引的成本非常高(涉及set的遍历)，因此不支持更新。建议: 先删除索引，再添加新的索引。
        log.error("it index can not support update");
    }

    @Override
    public void delete(String key, Set<Long> val) {
        log.info("UnitItIndex, before delete: {}", unitItMap);

        // 删除倒排索引
        Set<Long> unitIds = CommonUtils.getOrCreate(key, itUnitMap, ConcurrentSkipListSet::new);
        unitIds.removeAll(val);

        // 删除正向索引
        for (Long unitId : val) {
            Set<String> itTagSet = CommonUtils.getOrCreate(unitId, unitItMap, ConcurrentSkipListSet::new);
            itTagSet.remove(key);
        }

        log.info("UnitItIndex, after delete: {}", unitItMap);

    }

    // 匹配方法: 用于判断adUnit是否包含itTags.
    public boolean match(Long unitId, List<String> itTags) {
        Set<String> unitIts = unitItMap.get(unitId);

        if (CollectionUtils.isNotEmpty(unitIts)) {
            return CollectionUtils.isSubCollection(itTags, unitIts);
        }
        return false;
    }
}
