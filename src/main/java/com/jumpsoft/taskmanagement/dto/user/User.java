package com.jumpsoft.taskmanagement.dto.user;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Represents a user")
public record User(
        @Schema(description = "Unique identifier of the user", example = "1")
        @NotNull
        Long id,

        @Schema(description = "Username of the user", example = "pmarks")
        @NotNull
        @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
        String username,

        @Schema(description = "Full name of the user", example = "Peter Marks")
        @NotNull
        @Size(max = 40, message = "Username must be at most 40 characters")
        String fullName) {

    public User(Long id, String username, String fullName) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
    }
}
