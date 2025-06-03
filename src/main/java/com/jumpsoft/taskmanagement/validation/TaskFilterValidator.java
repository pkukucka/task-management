package com.jumpsoft.taskmanagement.validation;

import com.jumpsoft.taskmanagement.dto.TaskFilter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for the {@link ValidTaskFilter} annotation.
 * Verifies that a TaskFilter has at least one criterion specified.
 */
public class TaskFilterValidator implements ConstraintValidator<ValidTaskFilter, TaskFilter> {

    @Override
    public void initialize(ValidTaskFilter constraintAnnotation) {
        // No initialization needed
    }

    @Override
    public boolean isValid(TaskFilter taskFilter, ConstraintValidatorContext context) {
        if (taskFilter == null) {
            return true; // Null validation is handled by @NotNull if needed
        }
        
        return taskFilter.hasAnyCriteria();
    }
}