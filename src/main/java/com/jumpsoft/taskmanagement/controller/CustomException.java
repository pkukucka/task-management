package com.jumpsoft.taskmanagement.controller;


import com.jumpsoft.taskmanagement.dto.error.ErrorCode;

import lombok.Getter;

/* * CustomException is a custom exception class that extends the Exception class.
 * It is used to represent application-specific exceptions with an associated error code and optional data.
 * This class can be used to provide more context about the error that occurred.
 */

public class CustomException extends Exception {

    @Getter
    private ErrorCode errorCode;

    @Getter
    private String[] data;

    public CustomException(ErrorCode errorCode, String... data) {
        super();
        this.errorCode = errorCode;
        this.data = data == null ? new String[0] : data;
    }
}
