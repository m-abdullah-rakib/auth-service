package com.dp.authservice.exception;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.Collectors;

@Hidden
@ControllerAdvice
public class ApiExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        logger.error("User not found: {}", ex.getMessage(), ex);
        ApiErrorResponse response = createErrorResponse(HttpStatus.NOT_FOUND, "User Not Found", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream().map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage()).collect(Collectors.joining(", "));
        logger.error("Validation failed: {}", errorMessage, ex);

        ApiErrorResponse response = createErrorResponse(HttpStatus.BAD_REQUEST, "Validation Error", errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        logger.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        ApiErrorResponse response = createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static ApiErrorResponse createErrorResponse(HttpStatus status, String error, String message) {
        return new ApiErrorResponse(LocalDateTime.now(), status, error, message);
    }
}
