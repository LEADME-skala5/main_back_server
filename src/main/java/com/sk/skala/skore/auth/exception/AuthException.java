package com.sk.skala.skore.auth.exception;

import com.sk.skala.skore.base.BaseException;

public class AuthException extends BaseException {
    public AuthException(AuthExceptionType exceptionType) {
        super(exceptionType);
    }
}
