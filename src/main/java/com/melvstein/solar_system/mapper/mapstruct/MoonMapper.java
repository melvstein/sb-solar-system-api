package com.melvstein.solar_system.mapper.mapstruct;

import com.melvstein.solar_system.dto.MoonDto;
import com.melvstein.solar_system.model.Moon;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MoonMapper {
    MoonDto toDto(Moon moon);
    Moon toEntity(MoonDto dto);
}
