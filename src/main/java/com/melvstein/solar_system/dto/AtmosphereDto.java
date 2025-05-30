package com.melvstein.solar_system.dto;

public record AtmosphereDto(
        Long id,
        Double radius,
        String color,
        Double opacity,
        String emissive,
        Double emissiveIntensity
) {
}
