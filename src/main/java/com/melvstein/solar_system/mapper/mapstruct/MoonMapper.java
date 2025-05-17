package com.melvstein.solar_system.mapper.mapstruct;

import com.melvstein.solar_system.dto.MoonDto;
import com.melvstein.solar_system.model.Moon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MoonMapper {
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "radius", target = "radius"),
            @Mapping(source = "distance", target = "distance"),
            @Mapping(source = "speed", target = "speed"),
    })
    MoonDto toDto(Moon moon);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "radius", target = "radius"),
            @Mapping(source = "distance", target = "distance"),
            @Mapping(source = "speed", target = "speed"),
            @Mapping(target = "planet", ignore = true),
    })
    Moon toEntity(MoonDto dto);
}
