package com.melvstein.solar_system.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.melvstein.solar_system.constant.ApiConstants;
import com.melvstein.solar_system.dto.ApiResponse;
import com.melvstein.solar_system.dto.PlanetDto;
import com.melvstein.solar_system.model.Planet;
import com.melvstein.solar_system.service.PlanetService;
import com.melvstein.solar_system.util.ApiResponseUtils;
import com.melvstein.solar_system.util.Utils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/planets")
@Slf4j
public class PlanetController {

    private final PlanetService planetService;
    private final ObjectMapper objectMapper;

    public PlanetController(PlanetService planetService, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.planetService = planetService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<Page<PlanetDto>>> getPlanets(
            @RequestParam(required = false) Map<String, Object> params,
            @PageableDefault(size = 5) Pageable pageable
        ) {
        try {
             Page<PlanetDto> planets = planetService.getAllWithPageable(params, pageable);

             ApiResponse<Page<PlanetDto>> response = ApiResponseUtils.success(planets);

            log.info("{} response: {}", Utils.currentMethod(), objectMapper.writeValueAsString(response));

             return ResponseEntity.ok(response);
         } catch (Exception e) {
            log.error(Utils.currentMethod(), "error", e);

             return ResponseEntity
                     .status(HttpStatus.INTERNAL_SERVER_ERROR)
                     .body(ApiResponseUtils.error());
         }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse<PlanetDto>> getPlanetById(@PathVariable Long id) {
        try {
            Optional<PlanetDto> planet = planetService.getById(id);

            planet.ifPresentOrElse(
                    p -> {
                        try {
                            log.info("{} - id={}, planet={}", Utils.currentMethod(), id, objectMapper.writeValueAsString(p));
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    () -> log.warn("{} - id={}, planet not found", Utils.currentMethod(), id)
            );

            return planet
                    .map(p -> ResponseEntity.ok(ApiResponseUtils.success(p)))
                    .orElseGet(() -> ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body(ApiResponseUtils.error(ApiConstants.RESPONSE_ERROR.get("code"), "Planet not found", null))
                    );
        } catch (Exception e) {
            log.error(Utils.currentMethod(), "error", e);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseUtils.error());
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PlanetDto>> savePlanet(@RequestBody Planet planet) {
        try {
            PlanetDto existingPlanet = planetService.getByName(planet.getName());

            if (existingPlanet != null) {
                log.info("{} - request={}, existingPlanet={}", Utils.currentMethod(), objectMapper.writeValueAsString(planet), objectMapper.writeValueAsString(existingPlanet));

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(ApiResponseUtils.success(ApiConstants.RESPONSE_SUCCESS.get("code"), "Already Exists", existingPlanet));
            }

            PlanetDto newPlanet = planetService.save(planet);

            log.info("{} - request={}, newPlanet={}", Utils.currentMethod(), objectMapper.writeValueAsString(planet), objectMapper.writeValueAsString(newPlanet));

            return ResponseEntity.ok(ApiResponseUtils.success(newPlanet));
        } catch (Exception e) {
            log.error(Utils.currentMethod(), "error", e);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseUtils.error());
        }
    }

    @PostMapping("/bulk")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> saveAllPlanets (@RequestBody List<Planet> planets) {
        try {
            List<PlanetDto> existingPlanets = planetService.getAll(null);
            Set<String> existingPlanetNames = existingPlanets.stream().map(PlanetDto::name).collect(Collectors.toSet());

            List<Planet> insertPlanets = planets.stream().filter(p -> !existingPlanetNames.contains(p.getName())).toList();
            List<PlanetDto> newPlanets = planetService.saveAll(insertPlanets);

            Map<String, Object> data = new HashMap<>();
            data.put("count", newPlanets.size());
            data.put("items", newPlanets);

            return ResponseEntity.ok(ApiResponseUtils.success(data));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseUtils.error());
        }
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PlanetDto>> updatePlanetById(@PathVariable Long id, @RequestBody Planet planet) {
        try {
            PlanetDto result = planetService.updatePlanetById(id, planet);

            return ResponseEntity.ok(ApiResponseUtils.success(result));
        } catch (Exception e) {
            log.error(Utils.currentMethod(), "error", e);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseUtils.error());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deletePlanetById(@PathVariable Long id) {
        try {
            boolean exists = planetService.existsById(id);

            if (!exists) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseUtils.error(ApiConstants.RESPONSE_ERROR.get("code"), "Planet not found", null));
            }

            log.info("{} - id={}", Utils.currentMethod(), id);

            planetService.deleteById(id);

            return ResponseEntity.ok(ApiResponseUtils.success(ApiConstants.RESPONSE_SUCCESS.get("code"), "Deleted Successfully", null));
        } catch (Exception e) {
            log.error(Utils.currentMethod(), "error", e);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseUtils.error());
        }
    }
}
