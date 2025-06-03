package com.jumpsoft.taskmanagement.dto.error;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Represents an error response")
public record ResponseError(
		@Schema(description = "Error message", example = "Request operation not successful")
		String message,

		@ArraySchema(minItems = 0, schema = @Schema(implementation = ResponseErrorItem.class), arraySchema = @Schema(description = "List of custom error items"))
		List<ResponseErrorItem> errorItems
) {

	public static ResponseError of(String message) {
		return new ResponseError(message, Collections.emptyList());
	}

	public static ResponseError of(String message, ResponseErrorItem... errorItems) {
		return new ResponseError(message, new ArrayList<>(List.of(errorItems)));
	}

	public static ResponseError of(String message, List<ResponseErrorItem> errorItems) {
		return new ResponseError(message, errorItems != null ? errorItems : Collections.emptyList());
	}

}
