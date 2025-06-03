package com.jumpsoft.taskmanagement.dto.task;

import java.time.LocalDate;

import com.jumpsoft.taskmanagement.enums.BugSeverity;
import com.jumpsoft.taskmanagement.enums.TaskStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;



@Schema(description = "Represents a request to update a task in the system.")
public record TaskUpdateRequest(

        @Schema(description = "Name of the task", example = "Add dark mode feature")
        @Size(max = 100, message = "Task name must be at most 100 characters")
        String name,

        @Schema(description = "Detailed description of the task", example = "Implement dark mode across the application")
        @Size(max = 500, message = "Task description must be at most 500 characters")
        String description,

        @Schema(description = "Current status of the task", example = "OPEN")
        TaskStatus status,

        @Schema(description = "ID of the user who has assigned the task", example = "1")
        Long userId,

        @Schema(description = "The business value this feature will bring", example = "High user engagement")
        @Size(max = 100, message = "Task description can be at most 500 characters")
        String businessValue,

        @Schema(description = "Deadline by which the feature should be completed", example = "2024-12-31", format = "yyyy-MM-dd")
        @Future(message = "Deadline must be a future date")
        LocalDate deadline,

        @Schema(description = "Steps to reproduce the bug", example = "1. Navigate to login page, 2. Enter valid credentials, 3. Press the login button")
        String reproduceSteps,

        @Schema(description = "Severity level of the bug", example = "HIGH")
        BugSeverity severity
) {
}


