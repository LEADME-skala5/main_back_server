package com.sk.skala.skore.auth.user.exception;

public class TokenRefreshFailedException extends RuntimeException {
    public TokenRefreshFailedException(String message) {
        super(message);
    }
}
