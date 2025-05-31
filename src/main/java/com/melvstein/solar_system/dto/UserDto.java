package com.melvstein.solar_system.dto;

import jakarta.validation.constraints.NotBlank;

public record UserDto(
        Long id,

        @NotBlank(message = "Role is required")
        String role,

        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Password is required")
        String password
) {
}
