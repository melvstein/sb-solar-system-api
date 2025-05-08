package com.melvstein.solar_system.controller;

import com.melvstein.solar_system.constant.ApiConstants;
import com.melvstein.solar_system.dto.ApiResponse;
import com.melvstein.solar_system.dto.PlanetDto;
import com.melvstein.solar_system.model.Planet;
import com.melvstein.solar_system.repository.PlanetRepository;
import com.melvstein.solar_system.service.PlanetService;
import com.melvstein.solar_system.util.ApiResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/planets")
public class PlanetController {

    private final PlanetService planetService;

    public PlanetController(PlanetService planetService) {
        this.planetService = planetService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PlanetDto>>> getPlanets() {
        try {
             List<PlanetDto> planets = new ArrayList<>(planetService.getAll());
             ApiResponse<List<PlanetDto>> response = ApiResponseUtils.success(planets);

             return ResponseEntity.ok(response);
         } catch (Exception e) {
             return ResponseEntity
                     .status(HttpStatus.INTERNAL_SERVER_ERROR)
                     .body(ApiResponseUtils.error());
         }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PlanetDto>> getPlanetById(@PathVariable Long id) {
        try {
            Optional<PlanetDto> planet = planetService.getById(id);

            return planet
                    .map(p -> ResponseEntity.ok(ApiResponseUtils.success(p)))
                    .orElseGet(() -> ResponseEntity
                            .status(HttpStatus.NOT_FOUND)
                            .body(ApiResponseUtils.error(ApiConstants.RESPONSE_ERROR.get("code"), "Planet not found", null))
                    );
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseUtils.error());
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PlanetDto>> addPlanet(@RequestBody Planet planet) {
        try {
            PlanetDto existingPlanet = planetService.getByName(planet.getName());

            if (existingPlanet != null) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(ApiResponseUtils.success(ApiConstants.RESPONSE_SUCCESS.get("code"), "Already Exists", existingPlanet));
            }

            PlanetDto newPlanet = planetService.save(planet);

            return ResponseEntity.ok(ApiResponseUtils.success(newPlanet));
        } catch (Exception e) {
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

                return ResponseEntity.ok(ApiResponseUtils.success(planetService.save(updatedPlanet)));
            } else {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseUtils.error(ApiConstants.RESPONSE_ERROR.get("code"), "Planet not found", null));
            }

        } catch (Exception e) {
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

            planetService.deleteById(id);

            return ResponseEntity.ok(ApiResponseUtils.success(ApiConstants.RESPONSE_SUCCESS.get("code"), "Deleted Successfully", null));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseUtils.error());
        }
    }
}
