package com.melvstein.solar_system.dto;

public record MoonDto(
        Long id,
        String name,
        Double radius,
        Double distance,
        Double speed
) {
}
