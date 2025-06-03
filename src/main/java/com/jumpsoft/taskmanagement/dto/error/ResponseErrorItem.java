package com.jumpsoft.taskmanagement.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Schema(description = "Represents an error response item")
public record ResponseErrorItem(
		@Schema(description = "Custom error code", example = "E001") String code,
		@Schema(description = "Error message", example = "Username already exists") String message,
		@JsonInclude(JsonInclude.Include.NON_EMPTY)
		@Schema(description = "Field which is related to error", nullable = true, example = "username") String field
) {
	public static ResponseErrorItem of(String code, String message) {
		return new ResponseErrorItem(code, message, null);
	}

	public static ResponseErrorItem of(String code, String message, String field) {
		return new ResponseErrorItem(code, message, field);
	}
}
