package com.melvstein.solar_system.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.melvstein.solar_system.constant.ApiConstants;
import com.melvstein.solar_system.dto.PlanetDto;
import com.melvstein.solar_system.dto.UserDto;
import com.melvstein.solar_system.mapper.mapstruct.PlanetMapper;
import com.melvstein.solar_system.mapper.mapstruct.UserMapper;
import com.melvstein.solar_system.model.Planet;
import com.melvstein.solar_system.model.User;
import com.melvstein.solar_system.service.PlanetService;
import com.melvstein.solar_system.service.UserService;
import com.melvstein.solar_system.util.Utils;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("test-h2")
@AutoConfigureMockMvc
public class PlanetControllerTest {

    private static final Logger log = LoggerFactory.getLogger(PlanetControllerTest.class);
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PlanetService planetService;

    @Autowired
    private PlanetMapper planetMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    private static final String apiPath = "/api/v1/planets";

    private String jwtToken;

    @BeforeEach
    public void setUp() throws Exception {
        // List<Planet> planets = Utils.createPlanetEntities();
        // List<Planet> result = planetRepository.saveAll(planets);
        // System.out.println("Created Planets:" + result);

        //entityManager.flush();
        //entityManager.clear();

        User user = User.builder()
                .role("admin")
                .username("test")
                .password("test123")
                .build();

        userService.register(userMapper.toDto(user));

        Map<String, String> requestLogin = new HashMap<>();
        requestLogin.put("username", user.getUsername());
        requestLogin.put("password", user.getPassword());

        String jsonRequest = objectMapper.writeValueAsString(requestLogin);

        // Login user to get JWT token
        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        JsonNode response = objectMapper.readTree(responseString);
        jwtToken = response.path("data").path("jwtToken").asText();
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
                        .header("Authorization", "Bearer " + jwtToken)
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
        Planet planet = Utils.createPlanetEntity();
        PlanetDto savedPlanet = planetService.save(planet);

        MvcResult result = mockMvc.perform(get(apiPath + "/" + savedPlanet.id())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
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
        assertEquals(savedPlanet.name(), name);

        log.info("Get planet by ID: {}", response);
    }

    @Tag("supported")
    @Test
    public void test_savePlanet() throws Exception {
        Planet planet = Utils.createPlanetEntity();
        String jsonRequest = objectMapper.writeValueAsString(planet);

        MvcResult result = mockMvc.perform(post(apiPath)
                        .header("Authorization", "Bearer " + jwtToken)
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
                        .header("Authorization", "Bearer " + jwtToken)
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
        Planet planetEntity = planetMapper.toEntity(savedPlanet);

        Planet updatePlanet = new Planet();
        updatePlanet.setId(planetEntity.getId());
        updatePlanet.setName(planetEntity.getName());
        updatePlanet.setRadius(0.7);
        updatePlanet.setDistance(planetEntity.getDistance());
        updatePlanet.setSpeed(planetEntity.getSpeed());
        updatePlanet.setAtmosphere(planetEntity.getAtmosphere());
        updatePlanet.setMoons(planetEntity.getMoons());
        updatePlanet.setRing(planetEntity.getRing());

        log.info("Updating planet by ID:\n{}", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(updatePlanet));

        String jsonRequest = objectMapper.writeValueAsString(updatePlanet);

        MvcResult result = mockMvc.perform(patch(apiPath + "/" + savedPlanet.id())
                        .header("Authorization", "Bearer " + jwtToken)
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

    @Tag("supported")
    @Test
    public void test_deletePlanetById() throws Exception {
        Planet planet = Utils.createPlanetEntity();
        PlanetDto savedPlanet = planetService.save(planet);

        mockMvc.perform(delete(apiPath + "/" + savedPlanet.id())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(ApiConstants.RESPONSE_SUCCESS.get("code")))
                .andReturn();
    }
}
