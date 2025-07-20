package com.sk.skala.skore.user.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.sk.skala.skore.base.BaseExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum UserExceptionType implements BaseExceptionType {

    USER_NOT_FOUND(NOT_FOUND, "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMPLOYEE_NUMBER(BAD_REQUEST, "이미 가입된 사번입니다."),
    DUPLICATE_TEAMS_EMAIL(BAD_REQUEST, "이미 가입된 Teams 이메일입니다."),
    ORGANIZATION_NOT_FOUND(NOT_FOUND, "존재하지 않는 조직입니다."),
    DEPARTMENT_NOT_FOUND(NOT_FOUND, "존재하지 않는 부서입니다."),
    DIVISION_NOT_FOUND(NOT_FOUND, "존재하지 않는 본부입니다."),
    INVALID_PASSWORD(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "리프레시 토큰을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
