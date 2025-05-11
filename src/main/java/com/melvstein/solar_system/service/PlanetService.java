package com.melvstein.solar_system.service;

import com.melvstein.solar_system.dto.PlanetDto;
import com.melvstein.solar_system.mapper.mapstruct.PlanetMapper;
import com.melvstein.solar_system.model.Planet;
import com.melvstein.solar_system.repository.PlanetRepository;
import com.melvstein.solar_system.specification.PlanetSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PlanetService {

    private final PlanetRepository planetRepository;
    private final PlanetMapper planetMapper;

    @Autowired
    public PlanetService(PlanetRepository planetRepository, PlanetMapper planetMapper) {
        this.planetRepository = planetRepository;
        this.planetMapper = planetMapper;
    }

    public List<PlanetDto> getAll(Map<String, Object> params) {
        Specification<Planet> spec = Specification.where(null);

        if (params.containsKey("hasMoon")) {
            spec = Specification.where(PlanetSpecification.hasMoon());
        }

        List<Planet> planets = planetRepository.findAll(spec);
        return planetMapper.toDto(planets);
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
