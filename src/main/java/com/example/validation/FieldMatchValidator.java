package com.example.validation;

import com.example.dto.UserRegistrationRequestDto;
import com.example.validation.annotation.FieldMatch;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FieldMatchValidator
        implements ConstraintValidator<FieldMatch, UserRegistrationRequestDto> {
    @Override
    public boolean isValid(
            UserRegistrationRequestDto userRegistrationRequestDto,
            ConstraintValidatorContext constraintValidatorContext
    ) {
        if (userRegistrationRequestDto == null) {
            return true;
        }
        return userRegistrationRequestDto.getPassword()
                .equals(userRegistrationRequestDto.getRepeatPassword());
    }
}
