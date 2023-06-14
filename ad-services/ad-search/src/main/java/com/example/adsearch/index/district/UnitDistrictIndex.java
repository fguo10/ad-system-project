package com.example.adsearch.index.district;

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
import java.util.stream.Collectors;


@Slf4j
@Component
public class UnitDistrictIndex implements IndexAware<String, Set<Long>> {

    private static final Map<String, Set<Long>> districtUnitMap;
    private static final Map<Long, Set<String>> unitDistrictMap;

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

        Set<Long> unitIds = CommonUtils.getorCreate(key, districtUnitMap, ConcurrentSkipListSet::new);
        unitIds.addAll(val);

        for (Long unitId : val) {
            Set<String> districts = CommonUtils.getorCreate(unitId, unitDistrictMap, ConcurrentSkipListSet::new);
            districts.add(key);
        }

        log.info("UnitDistrictIndex, after add: {}", unitDistrictMap);
    }

    @Override
    public void update(String key, Set<Long> val) {
        log.error("district index can not support update");
    }

    @Override
    public void delete(String key, Set<Long> val) {

        log.info("UnitDistrictIndex, before delete: {}", unitDistrictMap);

        Set<Long> unitIds = CommonUtils.getorCreate(key, districtUnitMap, ConcurrentSkipListSet::new);
        unitIds.removeAll(val);

        for (Long unitId : val) {
            Set<String> districts = CommonUtils.getorCreate(unitId, unitDistrictMap, ConcurrentSkipListSet::new);
            districts.remove(key);
        }

        log.info("UnitDistrictIndex, after delete: {}", unitDistrictMap);
    }
}
