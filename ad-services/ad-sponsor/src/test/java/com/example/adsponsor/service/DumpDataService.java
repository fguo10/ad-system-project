package com.example.adsponsor.service;

import com.example.adcommon.dump.DumpConstant;
import com.example.adcommon.dump.table.*;
import com.example.adsponsor.constant.CommonStatus;
import com.example.adsponsor.entity.AdPlan;
import com.example.adsponsor.entity.AdUnit;
import com.example.adsponsor.entity.Creative;
import com.example.adsponsor.entity.adunit_condition.AdUnitDistrict;
import com.example.adsponsor.entity.adunit_condition.AdUnitIt;
import com.example.adsponsor.entity.adunit_condition.AdUnitKeyword;
import com.example.adsponsor.entity.adunit_condition.CreativeUnit;
import com.example.adsponsor.repository.AdPlanRepository;
import com.example.adsponsor.repository.AdUnitRepository;
import com.example.adsponsor.repository.CreativeRepository;
import com.example.adsponsor.repository.adunit_condition.AdUnitDistrictRepository;
import com.example.adsponsor.repository.adunit_condition.AdUnitItRepository;
import com.example.adsponsor.repository.adunit_condition.AdUnitKeywordRepository;
import com.example.adsponsor.repository.adunit_condition.CreativeUnitRepository;
import com.example.adsponsor.service.mapper.AdCreativeMapper;
import com.example.adsponsor.service.mapper.AdPlanMapper;
import com.example.adsponsor.service.mapper.AdUnitMapper;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.CollectionUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@Slf4j
@TestPropertySource("/application.yml")
@SpringBootTest
public class DumpDataService {
    @Autowired
    private AdPlanRepository adPlanRepository;
    @Autowired
    private AdUnitRepository adUnitRepository;
    @Autowired
    private CreativeRepository creativeRepository;
    @Autowired
    private CreativeUnitRepository creativeUnitRepository;
    @Autowired
    private AdUnitKeywordRepository adUnitKeywordRepository;
    @Autowired
    private AdUnitItRepository adUnitItRepository;
    @Autowired
    private AdUnitDistrictRepository adUnitDistrictRepository;


    public void dumpAdTableData() {

        File dir = new File(DumpConstant.DATA_ROOT_DIR);
        boolean dirCreated = dir.exists() || dir.mkdirs();
        if (!dirCreated) log.error("Failed to create data directory: {}", DumpConstant.DATA_ROOT_DIR);

        String dataRootDir = DumpConstant.DATA_ROOT_DIR;
        dumpAdPlanTable(dataRootDir + DumpConstant.AD_PLAN);
        dumpAdUnitTable(dataRootDir + DumpConstant.AD_UNIT);
        dumpAdCreativeTable(dataRootDir + DumpConstant.AD_CREATIVE);
        dumpAdCreativeUnitTable(dataRootDir + DumpConstant.AD_CREATIVE_UNIT);
        dumpAdUnitDistrictTable(dataRootDir + DumpConstant.AD_UNIT_DISTRICT);
        dumpAdUnitItTable(dataRootDir + DumpConstant.AD_UNIT_INTEREST);
        dumpAdUnitKeywordTable(dataRootDir + DumpConstant.AD_UNIT_KEYWORD);
    }


    public void dumpAdPlanTable(String filename) {
        List<AdPlan> adPlans = adPlanRepository.findAllByPlanStatus(CommonStatus.VALID.getStatus());
        if (adPlans == null || adPlans.isEmpty()) return;

        List<AdPlanTable> planTables = adPlans.stream().map(AdPlanMapper::toTable).toList();

        writeToFile(planTables, filename, AdPlanTable.class);
    }

    public void dumpAdUnitTable(String filename) {
        List<AdUnit> adUnits = adUnitRepository.findAllByUnitStatus(CommonStatus.VALID.getStatus());
        if (adUnits == null || adUnits.isEmpty()) return;

        List<AdUnitTable> unitTables = adUnits.stream().map(AdUnitMapper::toTable).toList();

        writeToFile(unitTables, filename, AdUnitTable.class);
    }


    public void dumpAdCreativeTable(String filename) {
        List<Creative> creatives = creativeRepository.findAll();
        if (creatives.isEmpty()) return;

        List<AdCreativeTable> creativeTables = creatives.stream().map(AdCreativeMapper::toTable).toList();

        writeToFile(creativeTables, filename, AdCreativeTable.class);
    }


    private void dumpAdCreativeUnitTable(String filename) {
        List<CreativeUnit> creativeUnits = creativeUnitRepository.findAll();
        if (CollectionUtils.isEmpty(creativeUnits)) return;

        List<AdCreativeUnitTable> creativeUnitTables = creativeUnits.stream().map(c -> new AdCreativeUnitTable(c.getCreativeId(), c.getUnitId())).toList();

        writeToFile(creativeUnitTables, filename, AdCreativeUnitTable.class);
    }

    private void dumpAdUnitDistrictTable(String filename) {

        List<AdUnitDistrict> unitDistricts = adUnitDistrictRepository.findAll();
        if (CollectionUtils.isEmpty(unitDistricts)) return;


        List<AdDistrictTable> unitDistrictTables = unitDistricts.stream().map(d -> new AdDistrictTable(d.getUnitId(), d.getState(), d.getCity())).toList();

        writeToFile(unitDistrictTables, filename, AdDistrictTable.class);
    }


    private void dumpAdUnitItTable(String filename) {
        List<AdUnitIt> unitIts = adUnitItRepository.findAll();
        if (CollectionUtils.isEmpty(unitIts)) return;

        List<AdUnitInterestTable> unitInterestTables = unitIts.stream().map(i -> new AdUnitInterestTable(i.getUnitId(), i.getItTag())).toList();
        writeToFile(unitInterestTables, filename, AdUnitInterestTable.class);
    }


    private void dumpAdUnitKeywordTable(String filename) {

        List<AdUnitKeyword> unitKeywords = adUnitKeywordRepository.findAll();
        if (CollectionUtils.isEmpty(unitKeywords)) return;

        List<AdUnitKeywordTable> unitKeywordTables = unitKeywords.stream().map(k -> new AdUnitKeywordTable(k.getUnitId(), k.getKeyword())

        ).toList();
        writeToFile(unitKeywordTables, filename, AdUnitKeywordTable.class);
    }

    private <T> void writeToFile(List<T> data, String filename, Class<T> dataType) {
        Path path = Paths.get(filename);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (T item : data) {
                String itemString;
                itemString = JSONValue.toJSONString(item);
                writer.write(itemString);
                writer.newLine();
            }
        } catch (IOException ex) {
            log.error("Dump {} error", filename);
        }
    }

}
