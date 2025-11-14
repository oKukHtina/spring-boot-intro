package com.example.validation;

import static com.example.constants.ApplicationConstant.ISBN_PATTERN;

import com.example.validation.annotation.Isbn;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsbnValidator implements ConstraintValidator<Isbn, String> {
    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext context) {
        if (isbn == null) {
            return false;
        }

        return isbn.matches(ISBN_PATTERN);
    }
}
