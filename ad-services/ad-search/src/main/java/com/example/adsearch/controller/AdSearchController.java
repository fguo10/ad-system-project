package com.example.adsearch.controller;

import com.example.adcommon.annotation.IgnoreResponseAdvice;
import com.example.adcommon.responseUtils.CommonResponse;
import com.example.adsearch.client.SponsorClient;
import com.example.adsearch.client.vo.AdPlan;
import com.example.adsearch.client.vo.AdPlanGetRequest;
import com.example.adsearch.search.SearchInterface;
import com.example.adsearch.search.vo.SearchRequest;
import com.example.adsearch.search.vo.SearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RestController
public class AdSearchController {
    private final RestTemplate restTemplate;
    private final SearchInterface searchInterface;
    private final SponsorClient sponsorClient;

    @Autowired
    public AdSearchController(SearchInterface searchInterface,
                              RestTemplate restTemplate,
                              SponsorClient sponsorClient) {
        this.searchInterface = searchInterface;
        this.restTemplate = restTemplate;
        this.sponsorClient = sponsorClient;
    }


    /**
     * 执行广告检索，获取广告创意响应结果
     *
     * @param request 广告检索请求对象
     * @return 广告检索响应对象
     */
    @PostMapping("/fetchCreative")
    public SearchResponse fetchCreative(@RequestBody SearchRequest request) {

        log.info("ad-search: fetchCreative -> {}", request.toString());
        return searchInterface.fetchCreative(request);
    }


    /**
     * 获取广告计划列表。
     *
     * @param request 包含广告计划获取请求的{@link AdPlanGetRequest}对象
     * @return 包含广告计划列表的{@link CommonResponse}对象
     */
    @IgnoreResponseAdvice
    @PostMapping("/getAdPlans")
    public CommonResponse<List<AdPlan>> getAdPlans(@RequestBody AdPlanGetRequest request) {
        log.info("ad-search: getAdPlans -> {}", request.toString());
        return sponsorClient.getAdPlans(request);
    }


    /**
     * 使用Ribbon获取广告计划列表。
     *
     * @param request 包含广告计划获取请求的{@link AdPlanGetRequest}对象
     * @return 包含广告计划列表的{@link CommonResponse}对象
     */
    @SuppressWarnings("all")
    @IgnoreResponseAdvice
    @PostMapping("/getAdPlansByRebbon")
    public CommonResponse<List<AdPlan>> getAdPlansByRebbon(@RequestBody AdPlanGetRequest request) {
        log.info("ad-search: getAdPlansByRebbon -> {}", request.toString());
        String url = "http://eureka-client-ad-sponsor/ad-sponsor/get/adPlan";
        CommonResponse response = restTemplate.postForEntity(url, request, CommonResponse.class).getBody();
        return response;
    }
}

