package com.example.adsearch.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@AllArgsConstructor
@RestController
public class AdSearchController {
    private RestTemplate restTemplate;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        return restTemplate.exchange("http://eureka-client-ad-sponsor/ad-sponsor/api/v1/ad_user",
                HttpMethod.GET, entity,
                String.class);
    }


}
