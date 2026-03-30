package com.example.pricetag.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException {

    private final String errorCode;
    private final HttpStatus httpStatus;

    public ApplicationException(String errorCode, String message, HttpStatus httpStatus) {
        super(message); // VERY IMPORTANT
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }
}