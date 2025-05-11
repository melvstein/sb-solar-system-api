package com.melvstein.solar_system.mapper.mapstruct;

import com.melvstein.solar_system.dto.PlanetDto;
import com.melvstein.solar_system.model.Planet;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                AtmosphereMapper.class,
                MoonMapper.class,
                RingMapper.class
        }
)
public interface PlanetMapper {
    PlanetDto toDto(Planet planet);
    Planet toEntity(PlanetDto dto);
    List<PlanetDto> toDto(List<Planet> planets);
    List<Planet> toEntity(List<PlanetDto> dtos);
}
