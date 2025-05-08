package com.melvstein.solar_system.service;

import com.melvstein.solar_system.dto.PlanetDto;
import com.melvstein.solar_system.mapper.PlanetMapper;
import com.melvstein.solar_system.model.Atmosphere;
import com.melvstein.solar_system.model.Planet;
import com.melvstein.solar_system.repository.AtmosphereRepository;
import com.melvstein.solar_system.repository.PlanetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PlanetService {

    private final PlanetRepository planetRepository;
    private final PlanetMapper planetMapper;
    private final AtmosphereRepository atmosphereRepository;

    @Autowired
    public PlanetService(PlanetRepository planetRepository, PlanetMapper planetMapper, AtmosphereRepository atmosphereRepository) {
        this.planetRepository = planetRepository;
        this.planetMapper = planetMapper;
        this.atmosphereRepository = atmosphereRepository;
    }

    public List<PlanetDto> getAll() {
        List<Planet> planets = planetRepository.findAll();
        return planetMapper.toDto(planets);
    }

    public Optional<PlanetDto> getById(Long id) {
        return planetRepository.findById(id).map(planetMapper::toDto);
    }

    public Optional<Planet> getEntityById(Long id) {
        return planetRepository.findById(id);
    }

    public PlanetDto save(Planet planet) {
        if (planet.getAtmosphere() != null) {
           planet.setAtmosphere(planet.getAtmosphere());
        }

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
