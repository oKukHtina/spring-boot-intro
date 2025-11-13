package com.example.validation;

import com.example.validation.annotation.Price;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class PriceValidator implements ConstraintValidator<Price, BigDecimal> {
    @Override
    public boolean isValid(BigDecimal price, ConstraintValidatorContext context) {
        if (price == null) {
            return false;
        }

        return price.compareTo(BigDecimal.ZERO) > 0;
    }
}
