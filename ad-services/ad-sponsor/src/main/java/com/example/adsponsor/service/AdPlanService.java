package com.example.adsponsor.service;

import com.example.adcommon.exception.AdException;
import com.example.adsponsor.entity.AdPlan;
import com.example.adsponsor.vo.AdPlanSearchRequest;

import java.util.List;

public interface AdPlanService {
    AdPlan createAdPlan(AdPlan request) throws AdException;

    List<AdPlan> getAdPlanByIds(AdPlanSearchRequest request) throws AdException;

    AdPlan updateAdPlan(Long id, AdPlan request) throws AdException;

    void deleteAdPlan(Long id) throws AdException;
}
