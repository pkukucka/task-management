package com.jumpsoft.taskmanagement.dto.task;

import java.time.LocalDate;

import com.jumpsoft.taskmanagement.enums.TaskCategory;
import com.jumpsoft.taskmanagement.enums.TaskStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Represents a request to create or update a feature task in the system.")
public record FeatureTaskCreateRequest(
        @Schema(description = "Name of the task", example = "Add dark mode feature", required = true)
        @Size(max = 100, message = "Task name must be at most 100 characters")
        @NotNull
        String name,

        @Schema(description = "Detailed description of the task", example = "Implement dark mode across the application")
        @Size(max = 500, message = "Task description must be at most 500 characters")
        String description,

        @Schema(description = "Current status of the task", example = "OPEN", required = true)
        @NotNull
        TaskStatus status,

        @Schema(description = "The business value this feature will bring", example = "High user engagement")
        @Size(max = 100, message = "Task description can be at most 500 characters")
        String businessValue,

        @Schema(description = "Deadline by which the feature should be completed", example = "2024-12-31", format = "yyyy-MM-dd", required = true)
        @NotNull
        @Future(message = "Deadline must be a future date")
        LocalDate deadline,

        @Schema(description = "ID of the user who has assigned the task", example = "1", required = true)
        @NotNull
        Long userId
) implements TaskCreateRequest {
    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public TaskStatus getStatus() {
        return status;
    }

    @Override
    public TaskCategory getCategory() {
        return TaskCategory.FEATURE;
    }

    @Override
    public Long getUserId() {
        return userId;
    }
}

