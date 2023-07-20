package com.example.adsponsor.controller;

import com.example.adcommon.exception.AdException;
import com.example.adsponsor.constant.Constants;
import com.example.adsponsor.entity.AdUser;
import com.example.adsponsor.entity.Creative;
import com.example.adsponsor.service.CreativeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/creative")
public class CreativeController {
    private CreativeService creativeService;

    @PostMapping
    public ResponseEntity<String> createCreative(@RequestBody Creative creative) throws AdException {
        log.info("ad-sponsor: createCreative -> {}", creative.toString());
        Creative createdCreative = creativeService.createCreative(creative);
        log.info("ad-sponsor: createCreative successfully -> {}", createdCreative.toString());
        return new ResponseEntity<>(Constants.SuccessMsg.CREATE_SUCCESS, HttpStatus.CREATED);
    }

}
