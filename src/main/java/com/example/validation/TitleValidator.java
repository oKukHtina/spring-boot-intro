package com.example.validation;

import static com.example.constants.ApplicationConstant.TITLE_PATTERN;

import com.example.validation.annotation.Title;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TitleValidator implements ConstraintValidator<Title, String> {
    @Override
    public boolean isValid(String title, ConstraintValidatorContext context) {
        if (title == null) {
            return false;
        }

        return title.matches(TITLE_PATTERN);
    }
}
