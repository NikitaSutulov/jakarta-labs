package com.team5.jakarta.api.validation;

import com.team5.jakarta.api.dto.ProductRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ValidationTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        if (factory != null) {
            factory.close();
        }
    }

    @Test
    void shouldFailOnInvalidSku() {
        ProductRequest request = validRequest();
        request.setName("**");

        int violations = validator.validate(request).size();
        Assertions.assertTrue(violations > 0);
    }

    @Test
    void shouldFailOnInvalidPriceRange() {
        ProductRequest request = validRequest();
        request.setDiscountPrice(150.0);
        request.setPrice(100.0);

        int violations = validator.validate(request).size();
        Assertions.assertTrue(violations > 0);
    }

    @Test
    void shouldPassOnValidRequest() {
        ProductRequest request = validRequest();
        request.setDiscountPrice(99.0);

        int violations = validator.validate(request).size();
        Assertions.assertEquals(0, violations);
    }

    private ProductRequest validRequest() {
        ProductRequest request = new ProductRequest();
        request.setName("BOSCH KGN39VI306");
        request.setDescription("Good fridge");
        request.setPrice(100.0);
        request.setImageUrl("https://example.com/a.jpg");
        request.setCategoryId(1);
        request.setAvailable(true);
        return request;
    }
}
