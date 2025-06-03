package com.jumpsoft.taskmanagement.controller;

import java.text.MessageFormat;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jumpsoft.taskmanagement.dto.error.ResponseError;
import com.jumpsoft.taskmanagement.dto.error.ResponseErrorItem;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

/**
 * CustomExceptionHandler is a global exception handler that intercepts exceptions thrown by the application.
 * It provides custom responses for specific exceptions, such as CustomException and validation errors.
 * This class extends ResponseEntityExceptionHandler to handle common Spring MVC exceptions.
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public final ResponseEntity<Object> handleAllExceptions(CustomException ex, WebRequest request) throws Exception {
		ResponseError responseError = ResponseError.of("Request operation not success due to CustomException",
				ResponseErrorItem.of(ex.getErrorCode().getCode(), MessageFormat.format(ex.getErrorCode().getMessage(), ex.getData())));
		log.error("Request operation not success due to CustomException {}", ex.getErrorCode());

		return new ResponseEntity(responseError, ex.getErrorCode().getHttpStatus());
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
    	ResponseError responseError = ResponseError.of("Internal Server Error");
		log.error("Internal server error", ex);
		return new ResponseEntity(responseError, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		ResponseError responseError = ResponseError.of("Validation of input request failed",
				Stream.concat(
						ex.getBindingResult().getGlobalErrors().stream()
				.map(e -> ResponseErrorItem.of(e.getCode(), e.getDefaultMessage())),
								ex.getBindingResult().getFieldErrors().stream()
				.map(e -> ResponseErrorItem.of(e.getCode(), e.getDefaultMessage(), e.getField()))).collect(Collectors.toList()));

		log.error("Validation of input request failed. ", ex);
		return new ResponseEntity(responseError, HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		ResponseError responseError = ResponseError.of("Validation of input request failed",
				ResponseErrorItem.of("BAD_REQUEST", ex.getMessage()));
		log.error("HttpMessageNotReadable ", ex);
		return new ResponseEntity(responseError, HttpStatus.BAD_REQUEST);
	}
}
