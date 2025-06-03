package com.jumpsoft.taskmanagement.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validates that a TaskFilter has at least one criterion specified.
 * A TaskFilter instance with all null fields is considered invalid.
 */
@Documented
@Constraint(validatedBy = com.jumpsoft.taskmanagement.validation.TaskFilterValidator.class)
@Target({ElementType.PARAMETER, ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTaskFilter {
    String message() default "At least one filter criterion must be specified";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}