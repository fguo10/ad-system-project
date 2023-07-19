package com.example.adsponsor.controller;

import com.example.adcommon.exception.AdException;
import com.example.adsponsor.constant.Constants;
import com.example.adsponsor.entity.Creative;
import com.example.adsponsor.service.DumpDataService;
import com.example.adsponsor.service.impl.DumpDataServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/dump")
public class DumpDataController {
    private DumpDataService dumpDataService;
    @GetMapping
    public ResponseEntity<String> dumpData() {
        dumpDataService.dumpAdTableData();
        return ResponseEntity.ok(Constants.SuccessMsg.DUMP_SUCCESS);
    }

}
