package com.example.adsearch.index.keyword;

import com.example.adsearch.index.IndexAware;
import com.example.adsearch.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;


// 实现正向索引和倒排索引的增删改查
@Slf4j
@Component
public class UnitKeywordIndex implements IndexAware<String, Set<Long>> {

    // 存储倒排索引, key is keyword, val is AdUnitsSet.
    private static final Map<String, Set<Long>> keywordUnitMap;
    // 存储正向索引, key is AdUnit, val is keywordSets. 一个adunit可能对应多个keyword
    private static final Map<Long, Set<String>> unitKeywordMap;


    // 使用线程安全的Map: thread safe, so we use concurrentHashMap
    static {
        keywordUnitMap = new ConcurrentHashMap<>();
        unitKeywordMap = new ConcurrentHashMap<>();
    }

    @Override
    public Set<Long> get(String key) {
        if (Objects.isNull(key) || key.isEmpty()) return Collections.emptySet();

        // 倒排索引的查看: 如果val存在,则返回val。 如果val不存在，则返回空集合。
        return keywordUnitMap.getOrDefault(key, Collections.emptySet());
    }

    @Override
    public void add(String keyword, Set<Long> unitIds) {
        log.info("UnitKeywordIndex, before add: {}", unitKeywordMap);

        // 添加倒排索引
        // ConcurrentSkipListSet: thread-safe, implement SortedSet.线程安全的集合。
        Set<Long> unitIdSet = CommonUtils.getOrCreate(keyword, keywordUnitMap, ConcurrentSkipListSet::new);
        unitIdSet.addAll(unitIds);

        // 添加正向索引
        for (Long unitId : unitIds) {
            Set<String> keywordSet = CommonUtils.getOrCreate(unitId, unitKeywordMap, ConcurrentSkipListSet::new);
            keywordSet.add(keyword);
        }

        log.info("UnitKeywordIndex, after add: {}", unitKeywordMap);
    }

    @Override
    public void update(String key, Set<Long> val) {
        // 更新索引的成本非常高(涉及set的遍历)，因此不支持更新。建议: 先删除索引，再添加新的索引。
        log.error("keyword index can not support update");
    }

    @Override
    public void delete(String keyword, Set<Long> unitIds) {
        log.info("UnitKeywordIndex, before delete: {}", unitKeywordMap);

        // 删除倒排索引
        Set<Long> allUnitIds = CommonUtils.getOrCreate(keyword, keywordUnitMap, ConcurrentSkipListSet::new);
        allUnitIds.removeAll(unitIds);

        // 删除正向索引
        for (Long unitId : unitIds) {
            Set<String> keywordSet = CommonUtils.getOrCreate(unitId, unitKeywordMap, ConcurrentSkipListSet::new);
            keywordSet.remove(keyword);
        }

        log.info("UnitKeywordIndex, after delete: {}", unitKeywordMap);
    }

    // 匹配方法: 用于判断adUnit是否包含keywords.
    public boolean match(Long unitId, List<String> keywords) {
        // 正向索引: 根据unitId获取该推广单元拥有的keywords.
        Set<String> unitKeywords = unitKeywordMap.get(unitId);
        if (CollectionUtils.isNotEmpty(unitKeywords)) {
            // 检查keywords是否为unitKeywords的子集? true:false
            return CollectionUtils.isSubCollection(keywords, unitKeywords);
        }
        return false;
    }
}
