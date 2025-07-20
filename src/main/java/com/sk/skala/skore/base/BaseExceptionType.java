package com.sk.skala.skore.base;

import org.springframework.http.HttpStatus;

public interface BaseExceptionType {
    String getMessage();

    HttpStatus getHttpStatus();
}
