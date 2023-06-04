package com.example.adsponsor.service.impl;

import com.example.adcommon.exception.AdException;
import com.example.adsponsor.constant.Constants;
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
import com.example.adsponsor.service.AdUnitService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class AdUnitServiceImpl implements AdUnitService {
    private final AdPlanRepository planRepository;
    private final AdUnitRepository unitRepository;

    private final AdUnitKeywordRepository unitKeywordRepository;
    private final AdUnitItRepository unitItRepository;
    private final AdUnitDistrictRepository unitDistrictRepository;

    private final CreativeRepository creativeRepository;
    private final CreativeUnitRepository creativeUnitRepository;


    @Override
    public AdUnit createAdUnit(AdUnit adUnit) throws AdException {
        Optional<AdPlan> adPlan = planRepository.findById(adUnit.getPlanId());
        if (adPlan.isEmpty()) {
            throw new AdException(Constants.ErrorMsg.CAN_NOT_FIND_RECORD);
        }

        AdUnit oldAdUnit = unitRepository.findByPlanIdAndUnitName(adUnit.getPlanId(), adUnit.getUnitName());
        if (oldAdUnit != null) {
            throw new AdException(Constants.ErrorMsg.SAME_NAME_UNIT_ERROR);
        }


        AdUnit adUnitObj = new AdUnit(adUnit.getPlanId(), adUnit.getUnitName(), adUnit.getPositionType(), adUnit.getBudget());

        return unitRepository.save(adUnitObj);
    }

    @Override
    public List<Long> createAdUnitKeyword(List<AdUnitKeyword> adUnitKeywordList) throws AdException {
        List<Long> unitIds = adUnitKeywordList.stream()
                .map(AdUnitKeyword::getUnitId)
                .toList();

        if (!isRelatedUnitExist(unitIds)) {
            throw new AdException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }

        List<Long> savedIds = new ArrayList<>(Collections.emptyList());

        List<AdUnitKeyword> unitKeywords = new ArrayList<>();
        if (!CollectionUtils.isEmpty(adUnitKeywordList)) {
            adUnitKeywordList.forEach(
                    i -> {
                        AdUnitKeyword adUnitKeywordObj = new AdUnitKeyword(i.getUnitId(), i.getKeyword());
                        AdUnitKeyword savedAdUnitKeyword = unitKeywordRepository.save(adUnitKeywordObj);
                        savedIds.add(savedAdUnitKeyword.getId());
                    }
            );
        }
        return savedIds;
    }


    @Override
    public AdUnit createAdUnitIt(AdUnitIt adUnitIt) throws AdException {
        return null;
    }

    @Override
    public AdUnitDistrict createAdUnitDistrict(AdUnitDistrict adUnitDistrict) throws AdException {
        return null;
    }

    @Override
    public CreativeUnit createAdCreativeUnit(CreativeUnit creativeUnit) throws AdException {
        return null;
    }


    private boolean isRelatedUnitExist(List<Long> unitIds) {

        if (CollectionUtils.isEmpty(unitIds)) {
            return false;
        }

        Set<Long> uniqueUnitIds = new HashSet<>(unitIds);
        List<AdUnit> units = unitRepository.findAllById(uniqueUnitIds);
        return units.size() == uniqueUnitIds.size();
    }


    private boolean isRelatedCreativeExist(List<Long> creativeIds) {

        if (CollectionUtils.isEmpty(creativeIds)) {
            return false;
        }

        Set<Long> uniquecreativeIds = new HashSet<>(creativeIds);
        List<Creative> creatives = creativeRepository.findAllById(uniquecreativeIds);
        return creatives.size() == uniquecreativeIds.size();
    }

}
