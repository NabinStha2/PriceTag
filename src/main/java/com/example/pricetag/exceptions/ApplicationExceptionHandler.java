package com.example.pricetag.exceptions;

import com.example.pricetag.dto.CommonResponseDto;
import com.example.pricetag.utils.ColorLogger;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationExceptionHandler {

    // Handle custom ApplicationException
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<CommonResponseDto<Void>> handleApplicationException(final ApplicationException exception,
                                                                              final HttpServletRequest request) {

        String guid = UUID
                .randomUUID()
                .toString();
        ColorLogger.logError(
                String.format("ApplicationException :: GUID=%s; message=%s", guid, exception.getMessage()));

        Map<String, Object> meta = new HashMap<>();
        meta.put("guid", guid);
        meta.put("path", request.getRequestURI());
        meta.put("method", request.getMethod());
        meta.put("timestamp", LocalDateTime.now());

        CommonResponseDto<Void> response = CommonResponseDto
                .<Void>builder()
                .message(exception.getMessage())
                .success(false)
                .status(exception
                                .getHttpStatus()
                                .value())
                .data(null)
                .meta(meta)
                .build();

        return new ResponseEntity<>(response, exception.getHttpStatus());
    }

    // Handle JWT Expired Exception
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<CommonResponseDto<Void>> handleExpiredJwtException(final ExpiredJwtException exception,
                                                                             final HttpServletRequest request) {

        String guid = UUID
                .randomUUID()
                .toString();
        ColorLogger.logError(String.format("ExpiredJwtException :: GUID=%s; message=%s", guid, exception.getMessage()));

        Map<String, Object> meta = new HashMap<>();
        meta.put("guid", guid);
        meta.put("path", request.getRequestURI());
        meta.put("method", request.getMethod());
        meta.put("timestamp", LocalDateTime.now());

        CommonResponseDto<Void> response = CommonResponseDto
                .<Void>builder()
                .message("JWT token has expired. Please login again.")
                .success(false)
                .status(HttpStatus.FORBIDDEN.value())
                .data(null)
                .meta(meta)
                .build();

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    // Handle unknown exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponseDto<Void>> handleUnknownException(final Exception exception,
                                                                          final HttpServletRequest request) {

        String guid = UUID
                .randomUUID()
                .toString();
        ColorLogger.logError(String.format("UnknownException :: GUID=%s; message=%s", guid, exception.getMessage()));

        Map<String, Object> meta = new HashMap<>();
        meta.put("guid", guid);
        meta.put("path", request.getRequestURI());
        meta.put("method", request.getMethod());
        meta.put("timestamp", LocalDateTime.now());

        CommonResponseDto<Void> response = CommonResponseDto.<Void>builder()
//                .message("An unexpected error occurred. Please contact support.")
                                                            .message(exception.getLocalizedMessage())
                                                            .success(false)
                                                            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                                            .data(null)
                                                            .meta(meta)
                                                            .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}