package com.example.adsponsor.controller;

import com.example.adcommon.exception.AdException;
import com.example.adsponsor.constant.Constants;
import com.example.adsponsor.entity.AdPlan;
import com.example.adsponsor.service.AdPlanService;
import com.example.adsponsor.vo.AdPlanSearchRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/ad_plan")
public class AdPlanController {


    private final AdPlanService adPlanService;

    @PostMapping()
    public ResponseEntity<String> createAdPlan(@RequestBody AdPlan request) throws AdException {
        log.info("ad-sponsor: createAdPlan -> {}", request.toString());
        AdPlan createdAdPlan = adPlanService.createAdPlan(request);
        log.info("ad-sponsor: createAdPlan -> {}", createdAdPlan.toString());
        return new ResponseEntity<>(Constants.SuccessMsg.CREATE_SUCCESS, HttpStatus.CREATED);

    }

    @GetMapping()
    public ResponseEntity<List<AdPlan>> getAdPlanByIds(@RequestBody AdPlanSearchRequest request) throws AdException {
        log.info("ad-sponsor: getAdPlanByIds -> {}", request.toString());
        List<AdPlan> adPlanList = adPlanService.getAdPlanByIds(request);
        return ResponseEntity.ok(adPlanList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdPlan> updateAdPlan(@PathVariable Long id, @RequestBody AdPlan request) throws AdException {
        log.info("ad-sponsor: updateAdPlan -> {}", request.toString());
        AdPlan updatedAdPlan = adPlanService.updateAdPlan(id, request);
        return ResponseEntity.ok(updatedAdPlan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAdPlan(@PathVariable Long id) throws AdException {
        log.info("ad-sponsor: deleteAdPlan -> {}", id);
        adPlanService.deleteAdPlan(id);
        return ResponseEntity.ok(Constants.SuccessMsg.DELETE_SUCCESS);
    }


}
