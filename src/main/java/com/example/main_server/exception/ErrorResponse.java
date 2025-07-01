package com.example.main_server.exception;

public record ErrorResponse(
        String errorCode,
        String message
) {
}

