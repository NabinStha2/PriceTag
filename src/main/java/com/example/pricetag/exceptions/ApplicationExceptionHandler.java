package com.example.pricetag.exceptions;

import ch.qos.logback.core.spi.ErrorCodes;
import com.example.pricetag.responses.CommonResponseDto;
import com.example.pricetag.utils.ColorLogger;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.UUID;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> handleApplicationException(final ApplicationException exception, final HttpServletRequest request) {
        var guid = UUID.randomUUID().toString();
        ColorLogger.logError(String.format("ApplicationException :: Error GUID=%s; error message: %s", guid, exception.getMessage()));
        var response = new ApiErrorResponse(guid, exception.getErrorCode(), exception.getMessage(), exception.getHttpStatus().value(), exception.getHttpStatus().name(), request.getRequestURI(), request.getMethod(), LocalDateTime.now());
        return new ResponseEntity<>(CommonResponseDto.builder().data(response).success(false).message(exception.getMessage()).build(), exception.getHttpStatus());
    }

//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity<?> handleUnAuthorizedException(
//            final UnAuthorizedException exception, final HttpServletRequest request) {
//        var guid = UUID.randomUUID().toString();
//        ColorLogger.logError(
//                String.format("UnAuthorizedException :: Error GUID=%s; error message: %s", guid, exception.getMessage()));
//        var response = new ApiErrorResponse(
//                guid,
//                "Authentication failed",
//                exception.getMessage(),
//                HttpStatus.UNAUTHORIZED.value(),
//                HttpStatus.UNAUTHORIZED.name(),
//                request.getRequestURI(),
//                request.getMethod(),
//                LocalDateTime.now());
//        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnknownException(final Exception exception, final HttpServletRequest request) {
        var guid = UUID.randomUUID().toString();
        ColorLogger.logError(String.format("Exception :: Error GUID=%s; error message: %s", guid, exception.getMessage()));
        var response = new ApiErrorResponse(guid, ErrorCodes.EMPTY_MODEL_STACK, exception.getMessage(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), request.getRequestURI(), request.getMethod(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ExpiredJwtException.class})
    public ResponseEntity<?> handleExpiredJwtException(ExpiredJwtException ex, HttpServletRequest request) {
        // String requestUri = ((ServletWebRequest)
        // request).getRequest().getRequestURI().toString();
        var guid = UUID.randomUUID().toString();
        var response = new ApiErrorResponse(guid, ErrorCodes.EMPTY_MODEL_STACK, ex.getMessage(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.name(), request.getRequestURI(), request.getMethod(), LocalDateTime.now());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

}
