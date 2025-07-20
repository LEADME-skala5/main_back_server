package com.sk.skala.skore.auth.user.exception;

public class LogoutFailedException extends RuntimeException {
    public LogoutFailedException(String message) {
        super(message);
    }
}