package com.melvstein.solar_system.mapper;

import com.melvstein.solar_system.dto.AtmosphereDto;
import com.melvstein.solar_system.model.Atmosphere;
import org.springframework.stereotype.Component;

@Component
public class AtmosphereMapper {
    public AtmosphereDto toDto(Atmosphere atmosphere) {
        if (atmosphere == null) return null;

        return AtmosphereDto.builder()
                .radius(atmosphere.getRadius())
                .color(atmosphere.getColor())
                .opacity(atmosphere.getOpacity())
                .emissive(atmosphere.getEmissive())
                .emissiveIntensity(atmosphere.getEmissiveIntensity())
                .build();
    }

    public Atmosphere toEntity(AtmosphereDto dto) {
        if (dto == null) return null;

        return Atmosphere.builder()
                .radius(dto.radius())
                .color(dto.color())
                .opacity(dto.opacity())
                .emissive(dto.emissive())
                .emissiveIntensity(dto.emissiveIntensity())
                .build();
    }
}
