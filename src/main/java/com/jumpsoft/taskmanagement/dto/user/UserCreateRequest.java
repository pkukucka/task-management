package com.jumpsoft.taskmanagement.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Represents a request for creation of the user")
public record UserCreateRequest(

        @Schema(description = "Username of the user", example = "pmarks")
        @NotNull
        @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
        String username,

        @Schema(description = "Full name of the user", example = "Peter Marks")
        @NotNull
        @Size(max = 40, message = "Username must be at most 40 characters")
        String fullName) { }
