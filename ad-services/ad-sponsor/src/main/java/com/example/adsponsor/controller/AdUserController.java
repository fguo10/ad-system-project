package com.example.adsponsor.controller;

import com.example.adcommon.exception.AdException;
import com.example.adsponsor.entity.AdUser;
import com.example.adsponsor.service.AdUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/ad_user")
public class AdUserController {
    private final AdUserService userService;

    @PostMapping
    public ResponseEntity<AdUser> createUser(@RequestBody AdUser adUser) throws AdException {
        log.info("ad-sponsor: createUser -> {}", adUser.toString());
        AdUser savedUser = userService.createUser(adUser);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping
    public String hello() {
        return "Hello";
    }
}
