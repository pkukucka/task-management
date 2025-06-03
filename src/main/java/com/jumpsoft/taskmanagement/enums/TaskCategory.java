package com.jumpsoft.taskmanagement.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true, description = "Possible categories of tasks")
public enum TaskCategory {
    BUG,
    FEATURE;
}
