package com.sk.skala.skore.auth.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.sk.skala.skore.base.BaseExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthExceptionType implements BaseExceptionType {

    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "리프레시 토큰을 찾을 수 없습니다."),
    REFRESH_TOKEN_MISMATCH(BAD_REQUEST, "리프레시 토큰이 일치하지 않습니다."),
    INVALID_REFRESH_TOKEN(BAD_REQUEST, "유효하지 않은 리프레시 토큰입니다."),
    LOGOUT_FAILED(INTERNAL_SERVER_ERROR, "로그아웃에 실패했습니다."),
    TOKEN_REFRESH_FAILED(INTERNAL_SERVER_ERROR, "토큰 갱신에 실패했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
