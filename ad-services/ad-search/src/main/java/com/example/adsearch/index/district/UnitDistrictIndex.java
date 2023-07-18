package com.example.adsearch.index.district;

import com.example.adsearch.index.IndexAware;
import com.example.adsearch.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;


@Slf4j
@Component
public class UnitDistrictIndex implements IndexAware<String, Set<Long>> {
    // 倒排索引: key is district(format is state-city), val is adUnitIds
    private static final Map<String, Set<Long>> districtUnitMap;
    // 正向索引: key is adUnitId, val is district(format is state-city)
    private static final Map<Long, Set<String>> unitDistrictMap;

    // 线程安全的Map
    static {
        districtUnitMap = new ConcurrentHashMap<>();
        unitDistrictMap = new ConcurrentHashMap<>();
    }

    @Override
    public Set<Long> get(String key) {
        return districtUnitMap.get(key);
    }

    @Override
    public void add(String key, Set<Long> val) {
        log.info("UnitDistrictIndex, before add: {}", unitDistrictMap);

        // 添加倒排索引
        Set<Long> unitIds = CommonUtils.getOrCreate(key, districtUnitMap, ConcurrentSkipListSet::new);
        unitIds.addAll(val);

        // 添加正向索引
        for (Long unitId : val) {
            Set<String> districts = CommonUtils.getOrCreate(unitId, unitDistrictMap, ConcurrentSkipListSet::new);
            districts.add(key);
        }

        log.info("UnitDistrictIndex, after add: {}", unitDistrictMap);
    }

    @Override
    public void update(String key, Set<Long> val) {
        // 更新索引的成本非常高(涉及set的遍历)，因此不支持更新。建议: 先删除索引，再添加新的索引。
        log.error("district index can not support update");
    }

    @Override
    public void delete(String key, Set<Long> val) {
        log.info("UnitDistrictIndex, before delete: {}", unitDistrictMap);

        // 删除倒排索引
        Set<Long> unitIds = CommonUtils.getOrCreate(key, districtUnitMap, ConcurrentSkipListSet::new);
        unitIds.removeAll(val);

        // 删除正向索引
        for (Long unitId : val) {
            Set<String> districts = CommonUtils.getOrCreate(unitId, unitDistrictMap, ConcurrentSkipListSet::new);
            districts.remove(key);
        }

        log.info("UnitDistrictIndex, after delete: {}", unitDistrictMap);
    }
}
