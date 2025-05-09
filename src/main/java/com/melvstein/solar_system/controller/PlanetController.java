package com.melvstein.solar_system.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.melvstein.solar_system.constant.ApiConstants;
import com.melvstein.solar_system.dto.ApiResponse;
import com.melvstein.solar_system.dto.PlanetDto;
import com.melvstein.solar_system.model.Planet;
import com.melvstein.solar_system.repository.PlanetRepository;
import com.melvstein.solar_system.service.PlanetService;
import com.melvstein.solar_system.util.ApiResponseUtils;
import com.melvstein.solar_system.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/planets")
@Slf4j
public class PlanetController {

    private final PlanetService planetService;
    private final ObjectMapper objectMapper;

    public PlanetController(PlanetService planetService, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.planetService = planetService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PlanetDto>>> getPlanets() {
        try {
             List<PlanetDto> planets = new ArrayList<>(planetService.getAll());
             ApiResponse<List<PlanetDto>> response = ApiResponseUtils.success(planets);

            log.info("{} response: {}", Util.currentMethod(), objectMapper.writeValueAsString(response));

             return ResponseEntity.ok(response);
         } catch (Exception e) {
            log.error(Util.currentMethod(), "error", e);

             return ResponseEntity
                     .status(HttpStatus.INTERNAL_SERVER_ERROR)
                     .body(ApiResponseUtils.error());
         }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PlanetDto>> getPlanetById(@PathVariable Long id) {
        try {
            Optional<PlanetDto> planet = planetService.getById(id);

            planet.ifPresentOrElse(
                    p -> {
                        try {
                            log.info("{} - id={}, planet={}", Util.currentMethod(), id, objectMapper.writeValueAsString(p));
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    () -> log.warn("{} - id={}, planet not found", Util.currentMethod(), id)
            );

            return planet
                    .map(p -> ResponseEntity.ok(ApiResponseUtils.success(p)))
                    .orElseGet(() -> ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body(ApiResponseUtils.error(ApiConstants.RESPONSE_ERROR.get("code"), "Planet not found", null))
                    );
        } catch (Exception e) {
            log.error(Util.currentMethod(), "error", e);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseUtils.error());
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PlanetDto>> savePlanet(@RequestBody Planet planet) {
        try {
            PlanetDto existingPlanet = planetService.getByName(planet.getName());

            if (existingPlanet != null) {
                log.info("{} - request={}, existingPlanet={}", Util.currentMethod(), objectMapper.writeValueAsString(planet), objectMapper.writeValueAsString(existingPlanet));

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(ApiResponseUtils.success(ApiConstants.RESPONSE_SUCCESS.get("code"), "Already Exists", existingPlanet));
            }

            PlanetDto newPlanet = planetService.save(planet);

            log.info("{} - request={}, newPlanet={}", Util.currentMethod(), objectMapper.writeValueAsString(planet), objectMapper.writeValueAsString(newPlanet));

            return ResponseEntity.ok(ApiResponseUtils.success(newPlanet));
        } catch (Exception e) {
            log.error(Util.currentMethod(), "error", e);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseUtils.error());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<PlanetDto>> updatePlanetById(@PathVariable Long id, @RequestBody Planet planet) {
        try {
            Optional<Planet> planetOptional = planetService.getEntityById(id);

            if (planetOptional.isPresent()) {
                Planet updatedPlanet = planetOptional.get();
                if (planet.getRadius() != null) {
                    updatedPlanet.setRadius(planet.getRadius());
                }

                if (planet.getDistance() != null) {
                    updatedPlanet.setDistance(planet.getDistance());
                }

                if (planet.getSpeed() != null) {
                    updatedPlanet.setSpeed(planet.getSpeed());
                }

                log.info("{} - id={}, request={}, updatedPlanet={}", Util.currentMethod(), id, objectMapper.writeValueAsString(planet), objectMapper.writeValueAsString(updatedPlanet));

                return ResponseEntity.ok(ApiResponseUtils.success(planetService.save(updatedPlanet)));
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseUtils.error(ApiConstants.RESPONSE_ERROR.get("code"), "Planet not found", null));
            }

        } catch (Exception e) {
            log.error(Util.currentMethod(), "error", e);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseUtils.error());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePlanetById(@PathVariable Long id) {
        try {
            boolean exists = planetService.existsById(id);

            if (!exists) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseUtils.error(ApiConstants.RESPONSE_ERROR.get("code"), "Planet not found", null));
            }

            log.info("{} - id={}", Util.currentMethod(), id);

            planetService.deleteById(id);

            return ResponseEntity.ok(ApiResponseUtils.success(ApiConstants.RESPONSE_SUCCESS.get("code"), "Deleted Successfully", null));
        } catch (Exception e) {
            log.error(Util.currentMethod(), "error", e);

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseUtils.error());
        }
    }
}
