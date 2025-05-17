package com.melvstein.solar_system.mapper.mapstruct;

import com.melvstein.solar_system.dto.PlanetDto;
import com.melvstein.solar_system.model.Planet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

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
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "radius", target = "radius"),
            @Mapping(source = "distance", target = "distance"),
            @Mapping(source = "speed", target = "speed"),
            @Mapping(source = "atmosphere", target = "atmosphere"),
            @Mapping(source = "moons", target = "moons"),
            @Mapping(source = "ring", target = "ring"),
    })
    PlanetDto toDto(Planet planet);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "radius", target = "radius"),
            @Mapping(source = "distance", target = "distance"),
            @Mapping(source = "speed", target = "speed"),
            @Mapping(source = "atmosphere", target = "atmosphere"),
            @Mapping(source = "moons", target = "moons"),
            @Mapping(source = "ring", target = "ring"),
    })
    Planet toEntity(PlanetDto dto);

    List<PlanetDto> toDtos(List<Planet> planets);
    List<Planet> toEntities(List<PlanetDto> dtos);
}
