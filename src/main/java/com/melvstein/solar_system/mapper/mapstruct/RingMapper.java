package com.melvstein.solar_system.mapper.mapstruct;

import com.melvstein.solar_system.dto.RingDto;
import com.melvstein.solar_system.model.Ring;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RingMapper {
    RingDto toDto(Ring ring);
    Ring toEntity(RingDto dto);
}
