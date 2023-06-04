package com.example.adsponsor.controller;


import com.example.adcommon.exception.AdException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;


@TestPropertySource("/application.yml")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class AdUnitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Transactional
    public void testCreateAdUnit() throws Exception {
        String requestBody = "{\"planId\": 1, \"unitName\": \"brand unit\", \"positionType\": 2, \"budget\": 1000}";

        ResultActions performCreateAdUnit1 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/ad_unit").contentType(MediaType.APPLICATION_JSON).content(requestBody));
        ResultActions performCreateAdUnit2 = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/ad_unit").contentType(MediaType.APPLICATION_JSON).content(requestBody));


        performCreateAdUnit1.andExpect(MockMvcResultMatchers.status().isCreated()).andExpect(MockMvcResultMatchers.jsonPath("$.unitName").value("brand unit"));
        performCreateAdUnit2.andExpect(MockMvcResultMatchers.status().isBadRequest()).andExpect(result -> Assertions.assertTrue(result.getResolvedException() instanceof AdException));
    }


    @Test
    public void testCreateUnitKeyword() throws AdException {

    }

    @Test
    public void testCreateUnitIt() throws AdException {

    }

    @Test
    public void testCreateUnitDistrict() throws AdException {

    }


    @Test
    public void testCreateCreativeUnit() throws AdException {

    }
}
