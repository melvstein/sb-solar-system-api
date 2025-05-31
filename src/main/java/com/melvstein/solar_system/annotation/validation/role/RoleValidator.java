package com.melvstein.solar_system.annotation.validation.role;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RoleValidator implements ConstraintValidator<RoleConstraint, String> {
    private Set<String> allowedRoles;

    @Override
    public void initialize(RoleConstraint constraintAnnotation) {
        this.allowedRoles = new HashSet<>(Arrays.asList(constraintAnnotation.value()));
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true; // Let @NotBlank handle null/empty

        boolean isValid = allowedRoles.contains(value.toLowerCase());

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Invalid role. Allowed values are: " + String.join(", ", allowedRoles)
            ).addConstraintViolation();
        }

        return isValid;
    }
}
