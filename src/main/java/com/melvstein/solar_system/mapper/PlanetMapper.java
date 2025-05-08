package com.melvstein.solar_system.mapper;

import com.melvstein.solar_system.dto.AtmosphereDto;
import com.melvstein.solar_system.dto.MoonDto;
import com.melvstein.solar_system.dto.PlanetDto;
import com.melvstein.solar_system.model.Atmosphere;
import com.melvstein.solar_system.model.Moon;
import com.melvstein.solar_system.model.Planet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlanetMapper {
    private final AtmosphereMapper atmosphereMapper;
    private final MoonMapper moonMapper;

    public PlanetMapper(AtmosphereMapper atmosphereMapper, MoonMapper moonMapper) {
        this.atmosphereMapper = atmosphereMapper;
        this.moonMapper = moonMapper;
    }

    public PlanetDto toDto(Planet planet) {
        if (planet == null) return null;

        AtmosphereDto atmosphereDto = atmosphereMapper.toDto(planet.getAtmosphere());

        List<MoonDto> moonDtos = planet.getMoons() != null
                ? planet.getMoons().stream()
                    .map(moonMapper::toDto)
                    .toList()
                : new ArrayList<>();

        return PlanetDto.builder()
                .name(planet.getName())
                .radius(planet.getRadius())
                .distance(planet.getDistance())
                .speed(planet.getSpeed())
                .atmosphere(atmosphereDto)
                .moons(moonDtos)
                .build();
    }

    public List<PlanetDto> toDto(List<Planet> planets) {
        return planets.stream().map(this::toDto).toList();
    }

    public Planet toEntity(PlanetDto dto) {
        if (dto == null) return null;

        Atmosphere atmosphere = atmosphereMapper.toEntity(dto.atmosphere());

        Planet planet = Planet.builder()
                .name(dto.name())
                .radius(dto.radius())
                .distance(dto.distance())
                .speed(dto.speed())
                .build();

        if (atmosphere != null) {
            planet.setAtmosphere(atmosphere);
        }

        if (dto.moons() != null) {
            List<Moon> moons = dto.moons().stream().map(moonDto -> {
                Moon moon = moonMapper.toEntity(moonDto);
                moon.setPlanet(planet);
                return moon;
            }).toList();

            planet.setMoons(moons);
        }

        return planet;
    }
}
