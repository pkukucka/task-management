package com.jumpsoft.taskmanagement.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Represents a request for user update")
public record UserUpdateRequest(

        @Schema(description = "Full name of the user", example = "Peter Marks")
        @Size(max = 40, message = "Username must be at most 40 characters")
        String fullName) { }
