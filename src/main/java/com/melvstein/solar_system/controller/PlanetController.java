package com.melvstein.solar_system.controller;

import com.melvstein.solar_system.constant.ApiConstants;
import com.melvstein.solar_system.dto.ApiResponse;
import com.melvstein.solar_system.model.Planet;
import com.melvstein.solar_system.repository.PlanetRepository;
import com.melvstein.solar_system.util.ApiResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/planets")
public class PlanetController {

    @Autowired
    private PlanetRepository planetRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Planet>>> getPlanets() {
        try {
             List<Planet> planets = new ArrayList<>(planetRepository.findAll());
             ApiResponse<List<Planet>> response = ApiResponseUtils.success(planets);

             return ResponseEntity.ok(response);
         } catch (Exception e) {
             return ResponseEntity
                     .status(HttpStatus.INTERNAL_SERVER_ERROR)
                     .body(ApiResponseUtils.error());
         }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Planet>> getPlanetById(@PathVariable Long id) {
        try {
            Optional<Planet> planet = planetRepository.findById(id);

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
    public ResponseEntity<ApiResponse<Planet>> addPlanet(@RequestBody Planet planet) {
        try {
            Planet existingPlanet = planetRepository.findByName(planet.getName());

            if (existingPlanet != null) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(ApiResponseUtils.error(ApiConstants.RESPONSE_ERROR.get("code"), "Planet already exists", null));
            }

            Planet newPlanet = planetRepository.save(planet);

            return ResponseEntity.ok(ApiResponseUtils.success(newPlanet));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseUtils.error());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Planet>> updatePlanetById(@PathVariable Long id, @RequestBody Planet planet) {
        try {
            Optional<Planet> planetOptional = planetRepository.findById(id);

            if (planetOptional.isPresent()) {
                Planet updatedPlanet = planetOptional.get();

                if (planet.getName() != null) {
                    updatedPlanet.setName(planet.getName());
                }

                if (planet.getRadius() != null) {
                    updatedPlanet.setRadius(planet.getRadius());
                }

                if (planet.getDistance() != null) {
                    updatedPlanet.setDistance(planet.getDistance());
                }

                if (planet.getSpeed() != null) {
                    updatedPlanet.setSpeed(planet.getSpeed());
                }

                return ResponseEntity.ok(ApiResponseUtils.success(planetRepository.save(updatedPlanet)));
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
            boolean exists = planetRepository.existsById(id);

            if (!exists) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(ApiResponseUtils.error(ApiConstants.RESPONSE_ERROR.get("code"), "Planet not found", null));
            }

            planetRepository.deleteById(id);

            return ResponseEntity.ok(ApiResponseUtils.success(ApiConstants.RESPONSE_SUCCESS.get("code"), "Deleted Successfully", null));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseUtils.error());
        }
    }
}
