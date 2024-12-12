package com.example.interestpointapi.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistingCategoryIdValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistingCategoryId {
    String message() default "Category does not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

