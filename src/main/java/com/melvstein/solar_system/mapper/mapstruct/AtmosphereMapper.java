package com.melvstein.solar_system.mapper.mapstruct;

import com.melvstein.solar_system.dto.AtmosphereDto;
import com.melvstein.solar_system.model.Atmosphere;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AtmosphereMapper {
    AtmosphereDto toDto(Atmosphere atmosphere);
    Atmosphere toEntity(AtmosphereDto dto);
}
