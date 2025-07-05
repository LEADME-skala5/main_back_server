package com.example.main_server.auth.user.exception;

public class TokenRefreshFailedException extends RuntimeException {
    public TokenRefreshFailedException(String message) {
        super(message);
    }
}
