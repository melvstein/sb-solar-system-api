package com.melvstein.solar_system.mapper;

import com.melvstein.solar_system.dto.builder.RingDto;
import com.melvstein.solar_system.model.Ring;
import org.springframework.stereotype.Component;

@Component
public class RingMapper {
    public RingDto toDto(Ring ring) {
        if (ring == null) return null;

        return RingDto.builder()
                .color(ring.getColor())
                .innerRadius(ring.getInnerRadius())
                .outerRadius(ring.getOuterRadius())
                .tilt(ring.getTilt())
                .opacity(ring.getOpacity())
                .build();
    }

    public Ring toEntity(RingDto dto) {
        if (dto == null) return null;

        return Ring.builder()
                .color(dto.color())
                .innerRadius(dto.innerRadius())
                .outerRadius(dto.outerRadius())
                .tilt(dto.tilt())
                .opacity(dto.opacity())
                .build();
    }
}
