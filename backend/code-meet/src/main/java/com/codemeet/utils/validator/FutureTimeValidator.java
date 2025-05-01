package com.codemeet.utils.validator;

import com.codemeet.utils.annotation.FutureTime;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;
import java.time.LocalDateTime;

public class FutureTimeValidator implements ConstraintValidator<FutureTime, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        return value != null && value.isAfter(LocalDateTime.now());
    }
}
