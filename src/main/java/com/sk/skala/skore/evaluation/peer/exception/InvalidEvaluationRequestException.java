package com.sk.skala.skore.evaluation.peer.exception;

public class InvalidEvaluationRequestException extends PeerEvaluationException {
    public InvalidEvaluationRequestException(String message) {
        super(PeerEvaluationExceptionType.INVALID_EVALUATION_REQUEST);
    }
}
