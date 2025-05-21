package com.melvstein.solar_system.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.melvstein.solar_system.dto.PlanetDto;
import com.melvstein.solar_system.mapper.mapstruct.PlanetMapper;
import com.melvstein.solar_system.model.Atmosphere;
import com.melvstein.solar_system.model.Moon;
import com.melvstein.solar_system.model.Planet;
import com.melvstein.solar_system.model.Ring;
import com.melvstein.solar_system.repository.PlanetRepository;
import com.melvstein.solar_system.specification.PlanetSpecification;
import com.melvstein.solar_system.util.Utils;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class PlanetService {

    private final PlanetRepository planetRepository;
    private final PlanetMapper planetMapper;
    private final ObjectMapper objectMapper;

    public PlanetService(PlanetRepository planetRepository, PlanetMapper planetMapper, ObjectMapper objectMapper) {
        this.planetRepository = planetRepository;
        this.planetMapper = planetMapper;
        this.objectMapper = objectMapper;
    }

    @Cacheable(value = "planetsCache", key = "'getallPlanets-' + T(com.melvstein.solar_system.util.Utils).generateCacheKeyFromParams(#params)")
    public List<PlanetDto> getAll(@Nullable Map<String, Object> params) {
        Specification<Planet> spec = Specification.where(null);

        if (params != null) {
            if (params.containsKey("hasAtmosphere") && params.containsKey("hasNoAtmosphere")) {
                spec = spec.and(PlanetSpecification.hasAtmosphere().or(PlanetSpecification.hasNoAtmosphere()));
            } else if (params.containsKey("hasAtmosphere") && !params.containsKey("hasNoAtmosphere")) {
                spec = spec.and(PlanetSpecification.hasAtmosphere());
            } else if (!params.containsKey("hasAtmosphere") && params.containsKey("hasNoAtmosphere")) {
                spec = spec.and(PlanetSpecification.hasNoAtmosphere());
            }

            if (params.containsKey("hasMoon") && params.containsKey("hasNoMoon")) {
                spec = spec.and(PlanetSpecification.hasMoon().or(PlanetSpecification.hasNoMoon()));
            } else if (params.containsKey("hasMoon") && !params.containsKey("hasNoMoon")) {
                spec = spec.and(PlanetSpecification.hasMoon());
            } else if (!params.containsKey("hasMoon") && params.containsKey("hasNoMoon")) {
                spec = spec.and(PlanetSpecification.hasNoMoon());
            }

            if (params.containsKey("hasRing") && params.containsKey("hasNoRing")) {
                spec = spec.and(PlanetSpecification.hasRing().or(PlanetSpecification.hasNoRing()));
            } else if (params.containsKey("hasRing") && !params.containsKey("hasNoRing")) {
                spec = spec.and(PlanetSpecification.hasRing());
            } else if (!params.containsKey("hasRing") && params.containsKey("hasNoRing")) {
                spec = spec.and(PlanetSpecification.hasNoRing());
            }
        }

        List<Planet> planets = planetRepository.findAll(spec);
        return planetMapper.toDtos(planets);
    }

    public Page<PlanetDto> getAllWithPageable(@Nullable Map<String, Object> params, Pageable pageable) {
        Specification<Planet> spec = Specification.where(null);

        if (params != null) {
            if (params.containsKey("hasAtmosphere")) {
                spec = spec.and(PlanetSpecification.hasAtmosphere());
            }

            if (params.containsKey("hasNoAtmosphere")) {
                spec = spec.and(PlanetSpecification.hasNoAtmosphere());
            }

            if (params.containsKey("hasMoon")) {
                spec = spec.and(PlanetSpecification.hasMoon());
            }

            if (params.containsKey("hasNoMoon")) {
                spec = spec.and(PlanetSpecification.hasNoMoon());
            }

            if (params.containsKey("hasRing")) {
                spec = spec.and(PlanetSpecification.hasRing());
            }

            if (params.containsKey("hasNoRing")) {
                spec = spec.and(PlanetSpecification.hasNoRing());
            }
        }

        Page<Planet> planets = planetRepository.findAll(spec, pageable);

        return planets.map(planetMapper::toDto);
    }

    public Optional<PlanetDto> getById(Long id) {
        return planetRepository.findById(id).map(planetMapper::toDto);
    }

    public Optional<Planet> getEntityById(Long id) {
        return planetRepository.findById(id);
    }

    @CacheEvict(value = "planetsCache", allEntries = true)
    public PlanetDto save(Planet planet) {
        Planet newPlanet = planetRepository.save(planet);

        return planetMapper.toDto(newPlanet);
    }

    @CacheEvict(value = "planetsCache", allEntries = true)
    public List<PlanetDto> saveAll(List<Planet> planets) {
       //List<Planet> planets = planetMapper.toEntities(planetDtos);
        List<Planet> newPlanets = planetRepository.saveAll(planets);

        return planetMapper.toDtos(newPlanets);
    }

    public void deleteById(Long id) {
        planetRepository.deleteById(id);
    }

    public PlanetDto getByName(String name) {
        Planet planet = planetRepository.findByName(name);
        return planetMapper.toDto(planet);
    }

    public boolean existsById(Long id) {
        return planetRepository.existsById(id);
    }

    public PlanetDto updatePlanetById(Long id, Planet planet) {
        try {
            Optional<Planet> planetOptional = getEntityById(id);

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

                if (planet.getAtmosphere() != null && updatedPlanet.getAtmosphere() != null) {
                    Atmosphere incomingAtmosphere = planet.getAtmosphere();
                    Atmosphere existingAtmosphere = updatedPlanet.getAtmosphere();

                    if (incomingAtmosphere.getRadius() != null) {
                        existingAtmosphere.setRadius(incomingAtmosphere.getRadius());
                    }

                    if (incomingAtmosphere.getColor() != null) {
                        existingAtmosphere.setColor(incomingAtmosphere.getColor());
                    }

                    if (incomingAtmosphere.getOpacity() != null) {
                        existingAtmosphere.setOpacity(incomingAtmosphere.getOpacity());
                    }

                    if (incomingAtmosphere.getEmissive() != null) {
                        existingAtmosphere.setEmissive(incomingAtmosphere.getEmissive());
                    }

                    if (incomingAtmosphere.getEmissiveIntensity() != null) {
                        existingAtmosphere.setEmissiveIntensity(incomingAtmosphere.getEmissiveIntensity());
                    }
                }

                if (planet.getMoons() != null && updatedPlanet.getMoons() != null && !planet.getMoons().isEmpty() && !updatedPlanet.getMoons().isEmpty() && planet.getMoons().size() == updatedPlanet.getMoons().size()) {
                    List<Moon> incomingMoons = planet.getMoons();
                    List<Moon> existingMoons = new ArrayList<>(updatedPlanet.getMoons());
                    updatedPlanet.setMoons(existingMoons);

                    incomingMoons.forEach((incomingMoon) -> {
                        existingMoons.forEach((existingMoon) -> {
                            if (incomingMoon.getId().equals(existingMoon.getId())) {
                                if (incomingMoon.getName() != null) {
                                    existingMoon.setName(incomingMoon.getName());
                                }

                                if (incomingMoon.getRadius() != null) {
                                    existingMoon.setRadius(incomingMoon.getRadius());
                                }

                                if (incomingMoon.getDistance() != null) {
                                    existingMoon.setDistance(incomingMoon.getDistance());
                                }

                                if (incomingMoon.getSpeed() != null) {
                                    existingMoon.setSpeed(incomingMoon.getSpeed());
                                }
                            }
                        });
                    });
                }

                if (planet.getRing() != null && updatedPlanet.getRing() != null) {
                    Ring incomingRing = planet.getRing();
                    Ring existingRing = updatedPlanet.getRing();

                    if (incomingRing.getColor() != null) {
                        existingRing.setColor(incomingRing.getColor());
                    }

                    if (incomingRing.getInnerRadius() != null) {
                        existingRing.setInnerRadius(incomingRing.getInnerRadius());
                    }

                    if (incomingRing.getOuterRadius() != null) {
                        existingRing.setOuterRadius(incomingRing.getOuterRadius());
                    }

                    if (incomingRing.getTilt() != null) {
                        existingRing.setTilt(incomingRing.getTilt());
                    }

                    if (incomingRing.getOpacity() != null) {
                        existingRing.setOpacity(incomingRing.getOpacity());
                    }
                }

                log.info("{} - id={}, request={}, updatedPlanet={}", Utils.currentMethod(), id, objectMapper.writeValueAsString(planet), objectMapper.writeValueAsString(updatedPlanet));

                PlanetDto result = save(updatedPlanet);

                return result;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("Error updating planet: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update planet", e);
        }
    }
}
