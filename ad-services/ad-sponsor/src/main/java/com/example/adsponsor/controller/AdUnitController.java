package com.example.adsponsor.controller;

import com.example.adcommon.exception.AdException;
import com.example.adsponsor.constant.Constants;
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
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{id}/keywords")
    public ResponseEntity<String> createUnitKeyword(@PathVariable Long id, @RequestBody List<String> keywordsList) throws AdException {
        log.info("ad-sponsor: createUnitKeyword -> {}", keywordsList.size());
        adUnitService.createAdUnitKeyword(id, keywordsList);
        return ResponseEntity.ok(Constants.SuccessMsg.RELATE_SUCCESS);
    }

    @PostMapping("/{id}/interests")
    public ResponseEntity<String> createUnitIt(@PathVariable Long id, @RequestBody List<String> interestsList) throws AdException {
        log.info("ad-sponsor: createUnitIt -> {}", interestsList.size());
        adUnitService.createAdUnitIt(id, interestsList);
        return ResponseEntity.ok(Constants.SuccessMsg.RELATE_SUCCESS);
    }

    @PostMapping("/{id}/areas")
    public ResponseEntity<String> createUnitDistrict(@PathVariable Long id, @RequestBody List<String> areasList) throws AdException {
        log.info("ad-sponsor: createUnitDistrict -> {}", areasList.size());
        adUnitService.createAdUnitDistrict(id, areasList);
        return ResponseEntity.ok(Constants.SuccessMsg.RELATE_SUCCESS);
    }


    @PostMapping("/creatives")
    public ResponseEntity<String> createCreativeUnit(@RequestBody List<CreativeUnit> creativesList) throws AdException {
        log.info("ad-sponsor: createCreativeUnit -> {}", creativesList.size());
        List<Long> savedIds = adUnitService.createAdCreativeUnit(creativesList);
        log.info("ad-sponsor: createCreativeUnit, savedIds = {}", savedIds);
        return ResponseEntity.ok(Constants.SuccessMsg.RELATE_SUCCESS);
    }

}
