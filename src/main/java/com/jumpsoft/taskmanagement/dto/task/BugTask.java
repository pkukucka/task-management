package com.jumpsoft.taskmanagement.dto.task;

import com.jumpsoft.taskmanagement.dto.user.User;
import com.jumpsoft.taskmanagement.enums.TaskCategory;
import com.jumpsoft.taskmanagement.enums.TaskStatus;
import com.jumpsoft.taskmanagement.enums.BugSeverity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents a bug task in the system.")
public record BugTask(
        @Schema(description = "Unique identifier of the task", example = "1")
        Long id,

        @Schema(description = "Name of the task", example = "Fix login bug", required = true)
        String name,

        @Schema(description = "Detailed description of the bug", example = "Resolve the issue causing 500 errors on login")
        String description,

        @Schema(description = "Current status of the task", example = "OPEN", required = true)
        TaskStatus status,

        @Schema(description = "Steps to reproduce the bug", example = "1. Navigate to login page, 2. Enter valid credentials, 3. Press the login button")
        String reproduceSteps,

        @Schema(description = "Severity level of the bug", example = "HIGH", required = true)
        BugSeverity severity,

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
        return TaskCategory.BUG;
    }

    @Override
    public User getUser() {
        return user;
    }
}

