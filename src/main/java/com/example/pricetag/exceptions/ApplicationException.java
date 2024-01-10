package com.example.pricetag.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class ApplicationException extends RuntimeException {
  private final String errorCode;
  private final String message;
  private final HttpStatus httpStatus;

  public ApplicationException(String errorCode, String message, HttpStatus httpStatus) {
    // super(String.format("%Error Code: %s || Message: %s || HttpStatus: %s",
    // errorCode, message, httpStatus.name()));
    this.errorCode = errorCode;
    this.message = message;
    this.httpStatus = httpStatus;
  }
}