package com.melvstein.solar_system.dto;

import lombok.Builder;

@Builder
public record AtmosphereDto(
        Double radius,
        String color,
        Double opacity,
        String emissive,
        Double emissiveIntensity
) {
}
