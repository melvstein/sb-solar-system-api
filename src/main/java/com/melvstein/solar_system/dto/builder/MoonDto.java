package com.melvstein.solar_system.dto.builder;

import lombok.Builder;

@Builder
public record MoonDto(
        Long id,
        String name,
        Double radius,
        Double distance,
        Double speed
) {
}
