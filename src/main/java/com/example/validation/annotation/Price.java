package com.example.validation.annotation;

import com.example.validation.PriceValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PriceValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Price {
    String message() default "Price must be greater than 0";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
