package com.melvstein.solar_system.annotation.validation.role;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoleValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleConstraint {
    String message() default "Invalid role. Allowed values are: {allowedRoles}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String[] value(); // allowed roles

    // Use this to customize error message with actual values
    String allowedRoles() default "";
}
