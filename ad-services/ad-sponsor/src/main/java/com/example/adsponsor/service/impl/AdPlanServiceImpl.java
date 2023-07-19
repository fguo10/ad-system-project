package com.example.adsponsor.service.impl;

import com.example.adcommon.exception.AdException;
import com.example.adsponsor.constant.CommonStatus;
import com.example.adsponsor.constant.Constants;
import com.example.adsponsor.entity.AdPlan;
import com.example.adsponsor.entity.AdUser;
import com.example.adsponsor.repository.AdPlanRepository;
import com.example.adsponsor.repository.AdUserRepository;
import com.example.adsponsor.service.AdPlanService;
import com.example.adsponsor.vo.AdPlanSearchRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AdPlanServiceImpl implements AdPlanService {

    private final AdUserRepository userRepository;
    private final AdPlanRepository planRepository;

    @Autowired
    public AdPlanServiceImpl(AdUserRepository userRepository, AdPlanRepository planRepository) {
        this.userRepository = userRepository;
        this.planRepository = planRepository;
    }

    @Override
    public AdPlan createAdPlan(AdPlan request) throws AdException {

        // 确保用户对象存在
        Optional<AdUser> adUser = userRepository.findById(request.getUserId());
        if (adUser.isEmpty())
            throw new AdException(Constants.ErrorMsg.USER_NOT_FOUND);

        AdPlan oldPlan = planRepository.findByUserIdAndPlanName(request.getUserId(), request.getPlanName());
        if (oldPlan != null)
            throw new AdException(Constants.ErrorMsg.SAME_NAME_PLAN_ERROR);

        AdPlan newAdPlan = planRepository.save(
                new AdPlan(request.getUserId(), request.getPlanName(),
                        request.getStartDate(),
                        request.getEndDate())
        );

        return newAdPlan;
    }

    @Override
    public List<AdPlan> getAdPlanByIds(AdPlanSearchRequest request) throws AdException {
        if (request.getAdPlanIds() == null || request.getAdPlanIds().isEmpty()) {
            return planRepository.findAllByUserId(request.getUserId());
        }
        return planRepository.findAllByIdInAndUserId(request.getAdPlanIds(), request.getUserId());
    }

    @Override
    public AdPlan updateAdPlan(Long id, AdPlan request) throws AdException {
        AdPlan plan = planRepository.findByIdAndUserId(request.getId(), request.getUserId());
        if (plan == null) {
            throw new AdException(Constants.ErrorMsg.AD_PLAN_NOT_FOUND);
        }

        if (request.getPlanName() != null) {
            plan.setPlanName(request.getPlanName());
        }

        if (request.getStartDate() != null) {
            plan.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            plan.setEndDate(request.getEndDate());
        }

        return planRepository.save(plan);
    }

    @Override
    public void deleteAdPlan(Long id) throws AdException {

        Optional<AdPlan> plan = planRepository.findById(id);
        if (plan.isEmpty()) {
            throw new AdException(Constants.ErrorMsg.AD_PLAN_NOT_FOUND);
        } else {
            plan.get().setPlanStatus(CommonStatus.INVALID.getStatus());
            planRepository.save(plan.get());
        }

    }
}
