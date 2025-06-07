package com.melvstein.solar_system.dto;

import java.io.Serial;
import java.io.Serializable;

public record RingDto(
        Long id,
        String color,
        Double innerRadius,
        Double outerRadius,
        Double tilt,
        Double opacity
)   implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
