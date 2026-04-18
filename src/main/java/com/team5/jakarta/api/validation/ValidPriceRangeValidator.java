package com.team5.jakarta.api.validation;

import com.team5.jakarta.api.dto.ProductRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidPriceRangeValidator implements ConstraintValidator<ValidPriceRange, ProductRequest> {

    @Override
    public boolean isValid(ProductRequest value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        if (value.getPrice() == null || value.getDiscountPrice() == null) {
            return true;
        }
        return value.getDiscountPrice() <= value.getPrice();
    }
}
