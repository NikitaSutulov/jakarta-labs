package com.team5.jakarta.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidPriceRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPriceRange {
    String message() default "discountPrice must be <= price";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
