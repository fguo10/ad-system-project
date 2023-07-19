package com.example.adsearch;

import com.example.adcommon.dump.DumpConstant;
import com.example.adcommon.dump.table.*;
import com.example.adsearch.handler.AdLevelDataHandler;
import com.example.adsearch.mysql.constant.OperateType;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.BiConsumer;


/*
IndexFileLoader负责加载和处理Ad Search模块的转储数据文件进行索引。
 */
@Slf4j
@Component
@DependsOn("dataTable")
public class IndexFileLoader {

    /**
     * 执行初始化和加载转储数据文件的操作。
     * 此方法使用@PostConstruct注解，确保在bean初始化之后执行。
     */
    @PostConstruct
    public void init() {

        initDir(DumpConstant.DATA_ROOT_DIR);

        initDir(DumpConstant.DATA_ROOT_DIR);

        loadAndHandleDumpData(DumpConstant.AD_PLAN, AdPlanTable.class, AdLevelDataHandler::handleLevel2);
        loadAndHandleDumpData(DumpConstant.AD_CREATIVE, AdCreativeTable.class, AdLevelDataHandler::handleLevel2);
        loadAndHandleDumpData(DumpConstant.AD_UNIT, AdUnitTable.class, AdLevelDataHandler::handleLevel3);
        loadAndHandleDumpData(DumpConstant.AD_CREATIVE_UNIT, AdCreativeUnitTable.class, AdLevelDataHandler::handleLevel3);
        loadAndHandleDumpData(DumpConstant.AD_UNIT_DISTRICT, AdDistrictTable.class, AdLevelDataHandler::handleLevel4);
        loadAndHandleDumpData(DumpConstant.AD_UNIT_INTEREST, AdUnitInterestTable.class, AdLevelDataHandler::handleLevel4);
        loadAndHandleDumpData(DumpConstant.AD_UNIT_KEYWORD, AdUnitKeywordTable.class, AdLevelDataHandler::handleLevel4);

    }

    /**
     * 加载并处理转储数据文件。
     *
     * @param fileName   转储数据文件名
     * @param tableClass 表格类的类型
     * @param handler    处理器函数接口
     * @param <T>        表格类型
     */
    private <T> void loadAndHandleDumpData(String fileName, Class<T> tableClass, BiConsumer<T, OperateType> handler) {
        List<String> dumpData = loadDumpData(DumpConstant.DATA_ROOT_DIR + fileName);
        dumpData.stream()
                .map(data -> JSON.parseObject(data, tableClass))
                .forEach(table -> handler.accept(table, OperateType.ADD));
    }


    /**
     * 初始化数据目录。
     *
     * @param dataRootDir 数据根目录路径
     */
    private void initDir(String dataRootDir) {
        try {
            Path dirPath = Paths.get(dataRootDir);
            Files.createDirectories(dirPath);
        } catch (IOException e) {
            log.error("Failed to create data directory: {}", dataRootDir);
        }
    }


    /**
     * 加载转储数据文件。
     *
     * @param fileName 文件名
     * @return 转储数据列表
     */
    private List<String> loadDumpData(String fileName) {
        try {
            // 文件不存在，则创建文件
            Path filePath = Paths.get(fileName);
            if (Files.notExists(filePath)) Files.createFile(filePath);
            // 返回文件的数据流并返回列表类型
            return Files.lines(filePath).toList();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
