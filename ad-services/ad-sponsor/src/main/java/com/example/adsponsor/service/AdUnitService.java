package com.example.adsponsor.service;

import com.example.adcommon.exception.AdException;
import com.example.adsponsor.entity.AdUnit;
import com.example.adsponsor.entity.adunit_condition.AdUnitDistrict;
import com.example.adsponsor.entity.adunit_condition.AdUnitIt;
import com.example.adsponsor.entity.adunit_condition.AdUnitKeyword;
import com.example.adsponsor.entity.adunit_condition.CreativeUnit;

import java.util.List;

public interface AdUnitService {
    AdUnit createAdUnit(AdUnit adUnit) throws AdException;

    List<Long> createAdUnitKeyword(List<AdUnitKeyword> adUnitKeywords) throws AdException;

    AdUnit createAdUnitIt(AdUnitIt adUnitIt) throws AdException;

    AdUnitDistrict createAdUnitDistrict(AdUnitDistrict adUnitDistrict) throws AdException;

    CreativeUnit createAdCreativeUnit(CreativeUnit creativeUnit) throws AdException;
}
