package com.melvstein.solar_system.dto.builder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

import lombok.Builder;

@Builder
public record PlanetDto(
        Long id,

        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Radius is required")
        Double radius,

        @NotNull(message = "Distance is required")
        Double distance,

        @NotNull(message = "Speed is required")
        Double speed,

        AtmosphereDto atmosphere,
        List<MoonDto> moons,
        RingDto ring
) {
}
