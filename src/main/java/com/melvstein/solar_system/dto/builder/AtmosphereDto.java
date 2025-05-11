package com.melvstein.solar_system.dto.builder;

import lombok.Builder;

@Builder
public record AtmosphereDto(
        Long id,
        Double radius,
        String color,
        Double opacity,
        String emissive,
        Double emissiveIntensity
) {
}
