package com.example.CollegeBackend.utils;

import java.time.LocalDateTime;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiError extends RuntimeException {
    private final HttpStatus status;
    private final String message;
    private final LocalDateTime timestamp;

    public ApiError(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

}
