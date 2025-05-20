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
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@ActiveProfiles("test-h2")
@AutoConfigureMockMvc
@SpringBootTest
public class PlanetControllerTest {

    private static final Logger log = LoggerFactory.getLogger(PlanetControllerTest.class);
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

    @Autowired
    private EntityManager entityManager;

    private PlanetDto planetDto;
    private static final String apiPath = "/api/v1/planets";

    @BeforeEach
    public void setUp() {
        List<Planet> planets = Utils.createPlanetEntities();
        // List<Planet> result = planetRepository.saveAll(planets);
        // System.out.println("Created Planets:" + result);

        //entityManager.flush();
        //entityManager.clear();
    }

    @AfterEach
    public void tearDown() {
        // planetRepository.deleteAll();
    }

    @Tag("supported")
    @Test
    public void test_getAllPlanets() throws Exception {
        List<Planet> planets = Utils.createPlanetEntities();
        planetService.saveAll(planets);

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

        log.info("Get Planets: {}", content);
    }

    @Tag("supported")
    @Test
    public void test_getPlanetById() throws Exception {
        List<Planet> planets = Utils.createPlanetEntities();
        planetService.saveAll(planets);

        MvcResult result = mockMvc.perform(get(apiPath + "/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        JsonNode response = objectMapper.readTree(responseString);
        String code = response.path("code").asText();
        String message = response.path("message").asText();
        JsonNode data = response.path("data");
        String name = data.get("name").asText();

        assertEquals(ApiConstants.RESPONSE_SUCCESS.get("code"), code);
        assertEquals(ApiConstants.RESPONSE_SUCCESS.get("message"), message);
        assertNotNull(data);
        assertFalse(data.isEmpty());

        log.info("Get planet by ID: {}", response);
    }

    @Tag("supported")
    @Test
    public void test_savePlanet() throws Exception {
        Planet planet = Utils.createPlanetEntity();
        String jsonRequest = objectMapper.writeValueAsString(planet);

        MvcResult result = mockMvc.perform(post(apiPath)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        JsonNode response = objectMapper.readTree(responseString);
        String code = response.path("code").asText();
        String message = response.path("message").asText();
        JsonNode data = response.path("data");

        assertEquals(ApiConstants.RESPONSE_SUCCESS.get("code"), code);
        assertEquals(ApiConstants.RESPONSE_SUCCESS.get("message"), message);
        assertNotNull(data);
        assertFalse(data.isEmpty());

        log.info("Saved result: {}", response);
    }

    @Tag("supported")
    @Test
    public void test_saveAllPlanets() throws Exception {
        List<Planet> planets = Utils.createPlanetEntities();
        String jsonRequest = objectMapper.writeValueAsString(planets);

        MvcResult result = mockMvc.perform(post(apiPath + "/bulk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        JsonNode response = objectMapper.readTree(responseString);
        String code = response.path("code").asText();
        String message = response.path("message").asText();
        JsonNode data = response.path("data");

        assertEquals(ApiConstants.RESPONSE_SUCCESS.get("code"), code);
        assertEquals(ApiConstants.RESPONSE_SUCCESS.get("message"), message);
        assertNotNull(data);
        assertFalse(data.isEmpty());

        log.info("Saved result: {}", response);
    }

    @Tag("supported")
    @Test
    public void test_updatePlanetById() throws Exception {
        Planet planet = Utils.createPlanetEntity();
        PlanetDto savedPlanet = planetService.save(planet);

        Planet updatePlanet = new Planet();
        updatePlanet.setRadius(0.7);

        log.info("Updating planet by ID:\n{}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(updatePlanet));

        String jsonRequest = objectMapper.writeValueAsString(updatePlanet);

        MvcResult result = mockMvc.perform(patch(apiPath + "/" + savedPlanet.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        JsonNode response = objectMapper.readTree(responseString);
        String code = response.path("code").asText();
        String message = response.path("message").asText();
        JsonNode data = response.path("data");

        assertEquals(ApiConstants.RESPONSE_SUCCESS.get("code"), code);
        assertEquals(ApiConstants.RESPONSE_SUCCESS.get("message"), message);
        assertNotNull(data);
        assertFalse(data.isEmpty());
        assertEquals(updatePlanet.getRadius(), data.get("radius").asDouble());

        log.info("Updated planet by ID:\n{}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response));
    }
}
