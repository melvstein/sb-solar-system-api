package com.melvstein.solar_system.dto;

import com.melvstein.solar_system.annotation.validation.role.RoleConstraint;
import jakarta.validation.constraints.NotBlank;

public record UserDto(
        Long id,

        @NotBlank(message = "Role is required")
        @RoleConstraint(value = {"admin", "user"})
        String role,

        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Password is required")
        String password
) {
}
