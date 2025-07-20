package com.sk.skala.skore.exception;

public record ErrorResponse(
        String errorCode,
        String message
) {
}

