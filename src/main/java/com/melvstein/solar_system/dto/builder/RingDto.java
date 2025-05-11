package com.melvstein.solar_system.dto.builder;

import lombok.Builder;

@Builder
public record RingDto(
        Long id,
        String color,
        Double innerRadius,
        Double outerRadius,
        Double tilt,
        Double opacity
) {
}
