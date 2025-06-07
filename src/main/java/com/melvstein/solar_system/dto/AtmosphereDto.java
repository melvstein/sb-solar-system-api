package com.melvstein.solar_system.dto;

import java.io.Serial;
import java.io.Serializable;

public record AtmosphereDto(
        Long id,
        Double radius,
        String color,
        Double opacity,
        String emissive,
        Double emissiveIntensity
)   implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
