package com.melvstein.solar_system.mapper;

import com.melvstein.solar_system.dto.builder.MoonDto;
import com.melvstein.solar_system.model.Moon;
import org.springframework.stereotype.Component;

@Component
public class MoonMapper {
    public MoonDto toDto(Moon moon) {
        if (moon == null) return null;

        return MoonDto.builder()
                .name(moon.getName())
                .radius(moon.getRadius())
                .distance(moon.getDistance())
                .speed(moon.getSpeed())
                .build();
    }

    public Moon toEntity(MoonDto dto) {
        if (dto == null) return null;

        return Moon.builder()
                .name(dto.name())
                .radius(dto.radius())
                .distance(dto.distance())
                .speed(dto.speed())
                .build();
    }
}
