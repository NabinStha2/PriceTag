package com.example.pricetag.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApiErrorResponse {
    private final String guid;         // unique trace ID
    private final String errorCode;    // e.g., "PRODUCT_NOT_FOUND"
    private final String message;      // user-friendly message
    private final Integer statusCode;  // e.g., 404
    private final String statusName;   // e.g., "NOT_FOUND"
    private final String path;         // request path
    private final String method;       // HTTP method
    private final LocalDateTime timestamp;
}