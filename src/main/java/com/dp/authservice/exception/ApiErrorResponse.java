package com.dp.authservice.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ApiErrorResponse(LocalDateTime timestamp, HttpStatus status, String error, String message) {
}
