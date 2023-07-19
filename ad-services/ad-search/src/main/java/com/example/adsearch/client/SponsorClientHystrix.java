package com.example.adsearch.client;

import com.example.adcommon.responseUtils.CommonResponse;
import com.example.adsearch.client.vo.AdPlan;
import com.example.adsearch.client.vo.AdPlanGetRequest;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 使用Hystrix回退机制, 用于处理错误并提供默认响应。
 */
@Component
public class SponsorClientHystrix implements SponsorClient {
    /**
     * 根据指定的请求获取广告计划列表。
     *
     * @param request 包含必要参数的{@link AdPlanGetRequest}对象
     * @return 包含广告计划列表的{@link CommonResponse}对象
     */
    @Override
    public CommonResponse<List<AdPlan>> getAdPlans(AdPlanGetRequest request) {
        return new CommonResponse<List<AdPlan>>(-1, "eureka-client-sponsor error");
    }
}
