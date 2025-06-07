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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlanetService {

    private final PlanetRepository planetRepository;
    private final PlanetMapper planetMapper;
    private final ObjectMapper objectMapper;
    private final CacheManager cacheManager;
    private final PlanetSpecification planetSpecification;
    private static final String CACHE_NAME = "planetsCache";

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

    //@Cacheable(value = CACHE_NAME, key = "'getAllWithPageable-' + T(com.melvstein.solar_system.util.Utils).generateCacheKeyFromParams(#params) + '-page:' + #pageable.pageNumber + '-size:' + #pageable.pageSize")
    public Page<PlanetDto> getAllWithPageable(@Nullable List<String> filter, Pageable pageable) {
        try {
            String cacheKey = "getAllWithPageable-" + Utils.generateCacheKeyFromFilter(filter);
            Cache cache = cacheManager.getCache(CACHE_NAME);

            log.info("{} - cacheName={} cacheKey={}", Utils.currentMethod(), CACHE_NAME, cacheKey);

            if (cache != null) {
                Page<?> cachedPageRaw = cache.get(cacheKey, Page.class);
                if (cachedPageRaw != null) {
                    @SuppressWarnings("unchecked")
                    Page<PlanetDto> cachedPage = (Page<PlanetDto>) cachedPageRaw;
                    log.info("{} - Cache hit!... cacheName={} cacheKey={}", Utils.currentMethod(), CACHE_NAME, cacheKey);
                    return cachedPage;
                }
            }

            Specification<Planet> spec = Specification.where(null);

            if (filter != null && !filter.isEmpty()) {
                if (filter.contains("hasMoon")) {
                    spec = spec.and(PlanetSpecification.hasMoon());
                }

                spec = spec.and(planetSpecification.getSpecificationByFilter(filter));
            }

            log.info("{} - Cache miss, fetching from DB... cacheName={} cacheKey={}", Utils.currentMethod(), CACHE_NAME, cacheKey);
            Page<Planet> planets = planetRepository.findAll(spec, pageable);
            Page<PlanetDto> dtoPage = planets.map(planetMapper::toDto);

            if (cache != null) {
                cache.put(cacheKey, dtoPage);
            }

            return dtoPage;
        } catch (Exception e) {
            log.error("{} - Error fetching planet: {}", Utils.currentMethod(), e.getMessage(), e);
            throw new RuntimeException("Failed to fetch planets", e);
        }
    }

    public Optional<PlanetDto> getById(Long id) {
        return planetRepository.findById(id).map(planetMapper::toDto);
    }

    public Optional<Planet> getEntityById(Long id) {
        return planetRepository.findById(id);
    }

    //@CacheEvict(value = CACHE_NAME, allEntries = true)
    public PlanetDto save(Planet planet) {
        Cache cache = cacheManager.getCache(CACHE_NAME);

        if (cache != null) {
            log.info("{} - cache cleared cacheName={}", Utils.currentMethod(), CACHE_NAME);
            cache.clear();
        }

        Planet newPlanet = planetRepository.save(planet);

        return planetMapper.toDto(newPlanet);
    }

    //@CacheEvict(value = CACHE_NAME, allEntries = true)
    public List<PlanetDto> saveAll(List<Planet> planets) {
        Cache cache = cacheManager.getCache(CACHE_NAME);

        if (cache != null) {
            log.info("{} - cache cleared cacheName={}", Utils.currentMethod(), CACHE_NAME);
            cache.clear();
        }

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

    //@CacheEvict(value = CACHE_NAME, allEntries = true)
    public PlanetDto updatePlanetById(Long id, Planet planet) {
        Cache cache = cacheManager.getCache(CACHE_NAME);

        if (cache != null) {
            log.info("{} - cache cleared cacheName={}", Utils.currentMethod(), CACHE_NAME);
            cache.clear();
        }

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

                return save(updatedPlanet);
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("{} - Error updating planet: {}", Utils.currentMethod(), e.getMessage(), e);
            throw new RuntimeException("Failed to update planet", e);
        }
    }
}
