package com.example.adsponsor.service;

import com.example.adcommon.exception.AdException;
import com.example.adsponsor.entity.AdUnit;
import com.example.adsponsor.entity.adunit_condition.AdUnitDistrict;
import com.example.adsponsor.entity.adunit_condition.AdUnitIt;
import com.example.adsponsor.entity.adunit_condition.AdUnitKeyword;
import com.example.adsponsor.entity.adunit_condition.CreativeUnit;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface AdUnitService {
    AdUnit createAdUnit(AdUnit adUnit) throws AdException;

    void createAdUnitKeyword(Long id, List<String> keywordsList) throws AdException;

    void createAdUnitIt(Long id, List<String> interestsList) throws AdException;

    void createAdUnitDistrict(Long id, List<String> districtsList) throws AdException;

//    void createAdCreativeUnit(Long id, List<Long> creativeIdsList) throws AdException;
}
