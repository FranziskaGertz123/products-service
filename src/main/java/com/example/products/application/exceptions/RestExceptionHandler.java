package com.example.products.application.exceptions;

import com.example.products.api.dto.ApiError;
import com.example.products.api.dto.Message;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public Message<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        return Message.of(
                new ApiError(ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
}
