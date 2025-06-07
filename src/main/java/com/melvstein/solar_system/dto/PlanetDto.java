package com.melvstein.solar_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

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
)  implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;
}
