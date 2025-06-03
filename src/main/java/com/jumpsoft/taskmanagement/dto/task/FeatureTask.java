package com.jumpsoft.taskmanagement.dto.task;

import java.time.LocalDate;

import com.jumpsoft.taskmanagement.dto.user.User;
import com.jumpsoft.taskmanagement.enums.TaskCategory;
import com.jumpsoft.taskmanagement.enums.TaskStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents a feature task in the system.")
public record FeatureTask(
        @Schema(description = "Unique identifier of the task", example = "1")
        Long id,

        @Schema(description = "Name of the task", example = "Add dark mode feature", required = true)
        String name,

        @Schema(description = "Detailed description of the task", example = "Implement dark mode across the application")
        String description,

        @Schema(description = "Current status of the task", example = "OPEN", required = true)
        TaskStatus status,

        @Schema(description = "The business value this feature will bring", example = "High user engagement")
        String businessValue,

        @Schema(description = "Deadline by which the feature should be completed", example = "2024-12-31", format = "yyyy-MM-dd")
        LocalDate deadline,

        @Schema(description = "User who has assigned the task")
        User user
) implements Task {
    @Override
    public Long getId() {
        return id;
    }

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
    public User getUser() {
        return user;
    }
}

