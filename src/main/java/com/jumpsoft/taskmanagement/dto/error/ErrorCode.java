package com.jumpsoft.taskmanagement.dto.error;

import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Schema(enumAsRef = true, description = "Represents a custom error code")
public enum ErrorCode {

    DUPLICATE_USERNAME("DUPLICATE_USERNAME", "Username {0} already exists", HttpStatus.CONFLICT),
    USER_WITH_ID_NOT_FOUND("USER_WITH_ID_NOT_FOUND", "User with the ID {0} not found", HttpStatus.NOT_FOUND);

    private String code;
    private String message;
    private HttpStatus httpStatus;

}