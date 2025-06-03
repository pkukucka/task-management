package com.jumpsoft.taskmanagement.dto.task;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.jumpsoft.taskmanagement.dto.user.User;
import com.jumpsoft.taskmanagement.enums.TaskCategory;
import com.jumpsoft.taskmanagement.enums.TaskStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "category"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BugTask.class, name = "BUG"),
        @JsonSubTypes.Type(value = FeatureTask.class, name = "FEATURE")
})
@Schema(description = "Task Data Transfer Object (DTO) for representing tasks in the system.")
public interface Task {
    @Schema(description = "Unique identifier of the task", example = "1")
    Long getId();

    @Schema(description = "Name of the task", example = "Fix login bug", required = true)
    String getName();

    @Schema(description = "Detailed description of the task", example = "Resolve the issue causing 500 errors on login")
    String getDescription();

    @Schema(description = "Current status of the task", example = "OPEN", required = true)
    TaskStatus getStatus();

    @Schema(description = "Category to identify the task type", example = "BUG", required = true)
    TaskCategory getCategory(); // To identify the type

    @Schema(description = "User who has assigned the task")
    User getUser();
}
