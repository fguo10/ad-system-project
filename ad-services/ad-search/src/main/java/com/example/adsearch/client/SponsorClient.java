package com.example.adsearch.client;

import com.example.adcommon.responseUtils.CommonResponse;
import com.example.adsearch.client.vo.AdPlan;
import com.example.adsearch.client.vo.AdPlanGetRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 广告投放系统客户端接口
 */

// 当调用出现失败或异常时，要使用fallback类。
@FeignClient(value = "eureka-client-ad-sponsor", fallback = SponsorClientHystrix.class)
public interface SponsorClient {

    /**
     * 获取广告计划列表
     *
     * @param request 请求对象，包含用户ID和广告计划ID列表
     * @return 响应对象，包含广告计划列表
     */
    @RequestMapping(value = "/ad-sponsor/get/adPlan", method = RequestMethod.POST)
    CommonResponse<List<AdPlan>> getAdPlans(@RequestBody AdPlanGetRequest request);

}
