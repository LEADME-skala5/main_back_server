package com.sk.skala.skore.user.exception;

import com.sk.skala.skore.base.BaseException;

public class UserException extends BaseException {

    public UserException(final UserExceptionType exceptionType) {
        super(exceptionType);
    }
}
