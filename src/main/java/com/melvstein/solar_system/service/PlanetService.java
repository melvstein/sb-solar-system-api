package com.melvstein.solar_system.service;

import com.melvstein.solar_system.dto.PlanetDto;
import com.melvstein.solar_system.mapper.mapstruct.PlanetMapper;
import com.melvstein.solar_system.model.Planet;
import com.melvstein.solar_system.repository.PlanetRepository;
import com.melvstein.solar_system.specification.PlanetSpecification;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PlanetService {

    private final PlanetRepository planetRepository;
    private final PlanetMapper planetMapper;

    public PlanetService(PlanetRepository planetRepository, PlanetMapper planetMapper) {
        this.planetRepository = planetRepository;
        this.planetMapper = planetMapper;
    }

    public List<PlanetDto> getAll(@Nullable Map<String, Object> params) {
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

    public PlanetDto save(Planet planet) {
        Planet newPlanet = planetRepository.save(planet);

        return planetMapper.toDto(newPlanet);
    }

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
}
