package com.team5.jakarta.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidSkuValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSku {
    String message() default "name must look like SKU/brand model (letters, digits, spaces, - and \")";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
