package com.sk.skala.skore.user.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.sk.skala.skore.base.BaseExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserExceptionType implements BaseExceptionType {

    USER_NOT_FOUND(NOT_FOUND, "사용자를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
