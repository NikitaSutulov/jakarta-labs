package com.team5.jakarta.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidSkuValidator implements ConstraintValidator<ValidSku, String> {

    private static final String PATTERN = "^[\\p{L}\\p{N}][\\p{L}\\p{N}\\s\\-\"']{2,119}$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.matches(PATTERN);
    }
}
