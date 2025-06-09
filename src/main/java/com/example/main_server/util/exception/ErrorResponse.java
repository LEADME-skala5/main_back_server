package com.example.main_server.util.exception;

public record ErrorResponse(
        String errorCode,
        String message
) {
}

