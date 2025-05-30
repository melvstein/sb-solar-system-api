package com.melvstein.solar_system.dto;

public record RingDto(
        Long id,
        String color,
        Double innerRadius,
        Double outerRadius,
        Double tilt,
        Double opacity
) {
}
