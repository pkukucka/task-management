package com.jumpsoft.taskmanagement.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true, description = "Possible statuses of tasks")
public enum TaskStatus {
    OPEN,
    IN_PROGRESS,
    DONE;
}
