package com.melvstein.solar_system.dto;

import lombok.Builder;

@Builder
public record RingDto(
        String color,
        Double innerRadius,
        Double outerRadius,
        Double tilt,
        Double opacity
) {
}
