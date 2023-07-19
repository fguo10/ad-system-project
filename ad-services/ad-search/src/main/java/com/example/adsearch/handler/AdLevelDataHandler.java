package com.example.adsearch.handler;

import com.example.adcommon.dump.table.*;
import com.example.adsearch.handler.mapper.AdCreativeMapper;
import com.example.adsearch.handler.mapper.AdPlanMapper;
import com.example.adsearch.handler.mapper.AdUnitMapper;
import com.example.adsearch.index.DataTable;
import com.example.adsearch.index.IndexAware;
import com.example.adsearch.index.adplan.AdPlanIndex;
import com.example.adsearch.index.adplan.AdPlanObject;
import com.example.adsearch.index.adunit.AdUnitIndex;
import com.example.adsearch.index.adunit.AdUnitObject;
import com.example.adsearch.index.creative.CreativeIndex;
import com.example.adsearch.index.creative.CreativeObject;
import com.example.adsearch.index.creativeunit.CreativeUnitIndex;
import com.example.adsearch.index.creativeunit.CreativeUnitObject;
import com.example.adsearch.index.district.UnitDistrictIndex;
import com.example.adsearch.index.interest.UnitInterestIndex;
import com.example.adsearch.index.keyword.UnitKeywordIndex;
import com.example.adsearch.mysql.constant.OperateType;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/*
该类负责处理和处理广告数据的不同层级的索引。包括处理 AdPlan、Creative、AdUnit 及其相关表的操作。
    1. 索引存在层级划分(依赖关系): Level 2, level 3....
    2. 加载全量索引是增量索引添加的特殊形式, eg: ADD、UPDATE、DELETE、OTHER
    3. 索引操作通过 IndexAware 接口进行，数据存储在 DataTable 中。
 */
@Slf4j
public class AdLevelDataHandler {

    // 对AdPlan的缓存操作: 索引默认存储到Table中，而管理缓存需要加载到内存使用object管理。 因此使用Mapper来转换。
    public static void handleLevel2(AdPlanTable planTable, OperateType operateType) {
        AdPlanObject planObject = AdPlanMapper.toObject(planTable);
        handleBinlogEvent(DataTable.of(AdPlanIndex.class), planObject.getPlanId(), planObject, operateType);
    }

    // 对creative的缓存操作： 索引默认存储到Table中，而管理缓存需要加载到内存使用object管理。 因此使用Mapper来转换。
    public static void handleLevel2(AdCreativeTable creativeTable, OperateType operateType) {
        CreativeObject creativeObject = AdCreativeMapper.toObject(creativeTable);
        handleBinlogEvent(DataTable.of(CreativeIndex.class), creativeObject.getAdId(), creativeObject, operateType);
    }


    // 处理 Level3 操作，根据操作类型对 AdUnitTable 进行索引处理(依赖于二级缓存AdPlan)。
    public static void handleLevel3(AdUnitTable unitTable, OperateType operateType) {
        AdPlanObject adPlanObject = DataTable.of(AdPlanIndex.class).get(unitTable.getPlanId());
        if (null == adPlanObject) {
            log.error("handleLevel3 found AdPlanObject error: {}", unitTable.getPlanId());
            return;
        }

        AdUnitObject adUnitObject = AdUnitMapper.toObject(unitTable, adPlanObject);
        handleBinlogEvent(DataTable.of(AdUnitIndex.class), adUnitObject.getUnitId(), adUnitObject, operateType);

    }


    // 处理 Level3 操作，根据操作类型对 AdCreativeUnitTable 进行索引处理。
    public static void handleLevel3(AdCreativeUnitTable creativeUnitTable, OperateType operateType) {
        if (operateType == OperateType.UPDATE) {
            log.error("CreateUnitIndex not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(creativeUnitTable.getUnitId());
        CreativeObject creativeObject = DataTable.of(CreativeIndex.class).get(creativeUnitTable.getCreativeId());

        if (null == unitObject || null == creativeObject) {
            log.error("AdCreativeUnitTable index error");
            return;
        }

        CreativeUnitObject creativeUnitObject = new CreativeUnitObject(
                creativeUnitTable.getCreativeId(),
                creativeUnitTable.getUnitId());

        String indexKey = String.format("%d_%d", creativeUnitObject.getCreativeId(), creativeUnitObject.getUnitId());
        handleBinlogEvent(DataTable.of(CreativeUnitIndex.class), indexKey, creativeUnitObject, operateType);
    }


    // 处理 Level4 操作，根据操作类型对 AdDistrictTable 进行索引处理。
    public static void handleLevel4(AdDistrictTable unitDistrictTable, OperateType operateType) {

        if (operateType == OperateType.UPDATE) {
            log.error("district index can not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitDistrictTable.getUnitId());
        if (null == unitObject) {
            log.error("AdUnitDistrictTable index error: {}", unitDistrictTable.getUnitId());
            return;
        }

        String indexKey = String.format("%s_%s", unitDistrictTable.getState(), unitDistrictTable.getCity());
        // 确保了集合中只包含一个元素
        Set<Long> indexValue = new HashSet<>(Collections.singleton(unitDistrictTable.getUnitId()));
        handleBinlogEvent(
                DataTable.of(UnitDistrictIndex.class),
                indexKey,
                indexValue,
                operateType
        );
    }


    // 处理 Level4 操作，根据操作类型对 AdUnitInterestTable 进行索引处理。
    public static void handleLevel4(AdUnitInterestTable unitInterestTable, OperateType operateType) {

        if (operateType == OperateType.UPDATE) {
            log.error("it index can not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitInterestTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitItTable index error: {}", unitInterestTable.getUnitId());
            return;
        }

        String indexKey = unitInterestTable.getItTag();
        Set<Long> indexValue = new HashSet<>(Collections.singleton(unitInterestTable.getUnitId()));
        handleBinlogEvent(DataTable.of(UnitInterestIndex.class),
                indexKey, indexValue, operateType
        );
    }

    // 处理 Level4 操作，根据操作类型对 AdUnitKeywordTable 进行索引处理。
    public static void handleLevel4(AdUnitKeywordTable keywordTable, OperateType operateType) {
        if (operateType == OperateType.UPDATE) {
            log.error("keyword index can not support update");
            return;
        }

        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(keywordTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitKeywordTable index error: {}", keywordTable.getUnitId());
            return;
        }
        String indexKey = keywordTable.getKeyword();

        Set<Long> indexValue = new HashSet<>(Collections.singleton(keywordTable.getUnitId()));
        handleBinlogEvent(DataTable.of(UnitKeywordIndex.class), indexKey, indexValue, operateType
        );
    }


    // 根据给定的OperateType,对索引执行相应的操作(ADD, UPDATE, DELETE, OTHER;)。
    private static <K, V> void handleBinlogEvent(IndexAware<K, V> index, K key, V value, OperateType type) {
        switch (type) {
            case ADD:
                index.add(key, value);
                break;
            case UPDATE:
                index.update(key, value);
            case DELETE:
                index.delete(key, value);
                break;
            case OTHER:
                break;
            default:
                break;
        }
    }
}
