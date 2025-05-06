package com.codemeet.utils.annotation;


import com.codemeet.utils.validator.FutureTimeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FutureTimeValidator.class)
@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FutureTime {

    String message() default "The start time must be in the future";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
