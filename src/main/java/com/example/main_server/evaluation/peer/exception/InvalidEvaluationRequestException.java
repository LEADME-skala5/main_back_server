package com.example.main_server.evaluation.peer.exception;

public class InvalidEvaluationRequestException extends RuntimeException {
    public InvalidEvaluationRequestException(String message) {
        super(message);
    }
}
