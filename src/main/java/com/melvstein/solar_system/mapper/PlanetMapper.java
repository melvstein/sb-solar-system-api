package com.melvstein.solar_system.mapper;

import com.melvstein.solar_system.dto.AtmosphereDto;
import com.melvstein.solar_system.dto.MoonDto;
import com.melvstein.solar_system.dto.PlanetDto;
import com.melvstein.solar_system.dto.RingDto;
import com.melvstein.solar_system.model.Atmosphere;
import com.melvstein.solar_system.model.Moon;
import com.melvstein.solar_system.model.Planet;
import com.melvstein.solar_system.model.Ring;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PlanetMapper {
    private final AtmosphereMapper atmosphereMapper;
    private final MoonMapper moonMapper;
    private final RingMapper ringMapper;

    public PlanetMapper(AtmosphereMapper atmosphereMapper, MoonMapper moonMapper, RingMapper ringMapper) {
        this.atmosphereMapper = atmosphereMapper;
        this.moonMapper = moonMapper;
        this.ringMapper = ringMapper;
    }

    public PlanetDto toDto(Planet planet) {
        if (planet == null) return null;

        AtmosphereDto atmosphereDto = atmosphereMapper.toDto(planet.getAtmosphere());

        List<MoonDto> moonDtos = planet.getMoons() != null
                ? planet.getMoons()
                    .stream()
                    .map(moonMapper::toDto)
                    .toList()
                : new ArrayList<>();

        RingDto ringDto = ringMapper.toDto(planet.getRing());

        return PlanetDto.builder()
                .name(planet.getName())
                .radius(planet.getRadius())
                .distance(planet.getDistance())
                .speed(planet.getSpeed())
                .atmosphere(atmosphereDto)
                .moons(moonDtos)
                .ring(ringDto)
                .build();
    }

    public List<PlanetDto> toDto(List<Planet> planets) {
        return planets.stream().map(this::toDto).toList();
    }

    public Planet toEntity(PlanetDto dto) {
        if (dto == null) return null;

        Atmosphere atmosphere = atmosphereMapper.toEntity(dto.atmosphere());

        List<Moon> moons = dto.moons() != null
                ? dto.moons()
                    .stream()
                    .map(moonMapper::toEntity)
                    .toList()
                : new ArrayList<>();

        Ring ring = ringMapper.toEntity(dto.ring());

        return Planet.builder()
                .name(dto.name())
                .radius(dto.radius())
                .distance(dto.distance())
                .speed(dto.speed())
                .atmosphere(atmosphere)
                .moons(moons)
                .ring(ring)
                .build();
    }
}
