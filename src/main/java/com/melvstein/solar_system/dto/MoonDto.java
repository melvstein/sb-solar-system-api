package com.melvstein.solar_system.dto;

import lombok.Builder;

@Builder
public record MoonDto(
        String name,
        Double radius,
        Double distance,
        Double speed
) {
}
