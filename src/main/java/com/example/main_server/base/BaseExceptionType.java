package com.example.main_server.base;

import org.springframework.http.HttpStatus;

public interface BaseExceptionType {
    String getMessage();

    HttpStatus getHttpStatus();
}
