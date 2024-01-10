package com.example.pricetag.exceptions;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiErrorResponse {
  private final String guid;
  private final String errorCode;
  private final String message;
  private final Integer statusCode;
  private final String statusName;
  private final String path;
  private final String method;
  private final LocalDateTime timestamp;

  public ApiErrorResponse(String guid, String errorCode, String message, Integer statusCode, String statusName,
      String path, String method, LocalDateTime timestamp) {
    this.guid = guid;
    this.errorCode = errorCode;
    this.message = message;
    this.statusCode = statusCode;
    this.statusName = statusName;
    this.path = path;
    this.method = method;
    this.timestamp = timestamp;
  }
}