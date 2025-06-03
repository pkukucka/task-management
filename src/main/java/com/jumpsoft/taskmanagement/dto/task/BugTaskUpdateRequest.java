package com.jumpsoft.taskmanagement.dto.task;

import com.jumpsoft.taskmanagement.enums.BugSeverity;
import com.jumpsoft.taskmanagement.enums.TaskCategory;
import com.jumpsoft.taskmanagement.enums.TaskStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record BugTaskUpdateRequest(
        @Schema(description = "Name of the task", example = "Add dark mode feature")
        @Size(max = 100, message = "Task name must be at most 100 characters")
        String name,

        @Schema(description = "Detailed description of the task", example = "Implement dark mode across the application")
        @Size(max = 500, message = "Task description must be at most 500 characters")
        String description,

        @Schema(description = "Current status of the task", example = "OPEN")
        TaskStatus status,

        @Schema(description = "Steps to reproduce the bug", example = "1. Navigate to login page, 2. Enter valid credentials, 3. Press the login button")
        String reproduceSteps,

        @Schema(description = "Severity level of the bug", example = "HIGH")
        BugSeverity severity,

        @Schema(description = "ID of the user who has assigned the task", example = "1")
        Long userId
) implements TaskUpdateRequest {
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
    public Long getUserId() {
        return userId;
    }

}

