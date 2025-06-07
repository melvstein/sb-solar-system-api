package com.melvstein.solar_system.dto;

import java.io.Serial;
import java.io.Serializable;

public record MoonDto(
        Long id,
        String name,
        Double radius,
        Double distance,
        Double speed
)   implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
