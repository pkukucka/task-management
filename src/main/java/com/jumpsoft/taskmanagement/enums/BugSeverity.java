package com.jumpsoft.taskmanagement.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Represents the severity level of a bug.
 * Severity indicates the impact of the bug on the system functionality.
 */
@Schema(enumAsRef = true, description = "Severity level of a bug, indicating its impact on the system functionality.")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum BugSeverity {
    
    CRITICAL("Critical"),
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low"),
    TRIVIAL("Trivial");

    @Getter
    private final String displayName;

}