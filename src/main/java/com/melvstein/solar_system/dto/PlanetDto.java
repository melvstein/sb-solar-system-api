package com.melvstein.solar_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record PlanetDto(
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
