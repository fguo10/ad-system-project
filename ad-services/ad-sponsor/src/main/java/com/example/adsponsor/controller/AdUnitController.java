package com.example.adsponsor.controller;

import com.example.adcommon.exception.AdException;
import com.example.adsponsor.entity.AdUnit;
import com.example.adsponsor.entity.adunit_condition.AdUnitDistrict;
import com.example.adsponsor.entity.adunit_condition.AdUnitIt;
import com.example.adsponsor.entity.adunit_condition.AdUnitKeyword;
import com.example.adsponsor.entity.adunit_condition.CreativeUnit;
import com.example.adsponsor.service.AdUnitService;
import com.example.adsponsor.service.AdUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/ad_unit")
public class AdUnitController {

    private final AdUnitService adUnitService;

    @PostMapping
    public ResponseEntity<AdUnit> createAdUnit(@RequestBody AdUnit adUnit) throws AdException {
        log.info("ad-sponsor: createAdUnit -> {}", adUnit.toString());
        AdUnit savedAdUnited = adUnitService.createAdUnit(adUnit);
        return new ResponseEntity<>(savedAdUnited, HttpStatus.CREATED);
    }

    @PostMapping("/unitKeyword")
    public ResponseEntity<List<Long>> createUnitKeyword(@RequestBody List<AdUnitKeyword> adUnitKeywordList) throws AdException {
        log.info("ad-sponsor: createUnitKeyword -> {}", adUnitKeywordList.size());
        List<Long> savedIds = adUnitService.createAdUnitKeyword(adUnitKeywordList);
        return new ResponseEntity<>(savedIds, HttpStatus.CREATED);
    }


    @PostMapping("/unitIt")
    public ResponseEntity<List<Long>> createUnitIt(@RequestBody List<AdUnitIt> adUnitItList) throws AdException {
        log.info("ad-sponsor: createUnitIt -> {}", adUnitItList.size());
        List<Long> savedIds = adUnitService.createAdUnitIt(adUnitItList);
        return new ResponseEntity<>(savedIds, HttpStatus.CREATED);
    }

    @PostMapping("/unitDistrict")
    public ResponseEntity<List<Long>> createUnitDistrict(@RequestBody List<AdUnitDistrict> adUnitDistrictList) throws AdException {
        log.info("ad-sponsor: createUnitDistrict -> {}", adUnitDistrictList.size());
        List<Long> savedIds = adUnitService.createAdUnitDistrict(adUnitDistrictList);
        return new ResponseEntity<>(savedIds, HttpStatus.CREATED);
    }


    @PostMapping("/creativeUnit")
    public ResponseEntity<List<Long>> createCreativeUnit(@RequestBody List<CreativeUnit> creativeUnitList) throws AdException {
        log.info("ad-sponsor: createCreativeUnit -> {}", creativeUnitList.size());
        List<Long> savedIds = adUnitService.createAdCreativeUnit(creativeUnitList);
        return new ResponseEntity<>(savedIds, HttpStatus.CREATED);
    }

}
