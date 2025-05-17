package com.melvstein.solar_system.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.melvstein.solar_system.constant.ApiConstants;
import com.melvstein.solar_system.dto.PlanetDto;
import com.melvstein.solar_system.mapper.mapstruct.PlanetMapper;
import com.melvstein.solar_system.model.Planet;
import com.melvstein.solar_system.repository.PlanetRepository;
import com.melvstein.solar_system.service.PlanetService;
import com.melvstein.solar_system.util.Utils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class PlanetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private PlanetService planetService;

    @Autowired
    private PlanetMapper planetMapper;

    private PlanetDto planetDto;
    private static final String apiPath = "/api/v1/planets";

    @BeforeEach
    public void setUp() {
        Planet planet = Utils.createPlanetEntity();
        // planetDto = planetService.save(planet);
    }

    @AfterEach
    public void tearDown() {

        // planetRepository.deleteAll();
    }

    @Tag("supported")
    @Test
    public void test_getAllPlanets() throws Exception {
        MvcResult result = mockMvc.perform(get(apiPath)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();

        String responseString = result.getResponse().getContentAsString();

        JsonNode response = objectMapper.readTree(responseString);
        String code = response.path("code").asText();
        String message = response.path("message").asText();
        JsonNode content = response.path("data").path("content");

        assertEquals(ApiConstants.RESPONSE_SUCCESS.get("code"), code);
        assertEquals(ApiConstants.RESPONSE_SUCCESS.get("message"), message);
        assertNotNull(content);
        assertFalse(content.isEmpty());
    }

    @Tag("supported")
    @Test
    public void test_getPlanetById() throws Exception {
        MvcResult result = mockMvc.perform(get(apiPath + "/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        JsonNode response = objectMapper.readTree(responseString);
        String code = response.path("code").asText();
        String message = response.path("message").asText();
        JsonNode data = response.path("data");
        String name = data.get("name").asText();

        System.out.println(planetDto);
        assertEquals(ApiConstants.RESPONSE_SUCCESS.get("code"), code);
        assertEquals(ApiConstants.RESPONSE_SUCCESS.get("message"), message);
        assertNotNull(data);
        assertFalse(data.isEmpty());
    }
}
