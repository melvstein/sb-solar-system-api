package com.melvstein.solar_system.mapper.mapstruct;

import com.melvstein.solar_system.dto.AtmosphereDto;
import com.melvstein.solar_system.model.Atmosphere;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface AtmosphereMapper {
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "radius", target = "radius"),
            @Mapping(source = "color", target = "color"),
            @Mapping(source = "opacity", target = "opacity"),
            @Mapping(source = "emissive", target = "emissive"),
            @Mapping(source = "emissiveIntensity", target = "emissiveIntensity"),
    })
    AtmosphereDto toDto(Atmosphere atmosphere);

    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "radius", target = "radius"),
            @Mapping(source = "color", target = "color"),
            @Mapping(source = "opacity", target = "opacity"),
            @Mapping(source = "emissive", target = "emissive"),
            @Mapping(source = "emissiveIntensity", target = "emissiveIntensity"),
            @Mapping(target = "planet", ignore = true),
    })
    Atmosphere toEntity(AtmosphereDto dto);
}
