package com.jumpsoft.taskmanagement.dto.task;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.jumpsoft.taskmanagement.enums.TaskCategory;
import com.jumpsoft.taskmanagement.enums.TaskStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "category"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BugTaskCreateRequest.class, name = "BUG"),
        @JsonSubTypes.Type(value = FeatureTaskCreateRequest.class, name = "FEATURE")
})
@Schema(description = "Represents a request to update a task in the system.")
public interface TaskUpdateRequest {
    @Schema(description = "Name of the task", example = "Fix login bug")
    @Size(max = 100, message = "Task name must be at most 100 characters")
    String getName();

    @Schema(description = "Detailed description of the task", example = "Resolve the issue causing 500 errors on login")
    @Size(max = 500, message = "Task name must be at most 500 characters")
    String getDescription();

    @Schema(description = "Current status of the task", example = "OPEN")
    TaskStatus getStatus();

    @Schema(description = "ID of the user who has assigned the task", example = "1")
    Long getUserId();
}
