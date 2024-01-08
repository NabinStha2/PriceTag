package com.example.pricetag.config.exceptions;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.pricetag.utils.ColorLogger;

import ch.qos.logback.core.spi.ErrorCodes;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity<?> handleApplicationException(
      final ApplicationException exception, final HttpServletRequest request) {
    var guid = UUID.randomUUID().toString();
    ColorLogger.logError(
        String.format("ApplicationException :: Error GUID=%s; error message: %s", guid, exception.getMessage()));
    var response = new ApiErrorResponse(
        guid,
        exception.getErrorCode(),
        exception.getMessage(),
        exception.getHttpStatus().value(),
        exception.getHttpStatus().name(),
        request.getRequestURI(),
        request.getMethod(),
        LocalDateTime.now());
    return new ResponseEntity<>(response, exception.getHttpStatus());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleUnknownException(
      final Exception exception, final HttpServletRequest request) {
    var guid = UUID.randomUUID().toString();
    ColorLogger.logError(
        String.format("Exception :: Error GUID=%s; error message: %s", guid, exception.getMessage()));
    var response = new ApiErrorResponse(
        guid,
        ErrorCodes.EMPTY_MODEL_STACK,
        exception.getMessage(),
        HttpStatus.BAD_REQUEST.value(),
        HttpStatus.BAD_REQUEST.name(),
        request.getRequestURI(),
        request.getMethod(),
        LocalDateTime.now());
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

}
