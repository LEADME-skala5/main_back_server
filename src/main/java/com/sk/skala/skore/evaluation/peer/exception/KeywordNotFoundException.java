package com.sk.skala.skore.evaluation.peer.exception;

public class KeywordNotFoundException extends PeerEvaluationException {
    public KeywordNotFoundException(String message) {
        super(PeerEvaluationExceptionType.KEYWORD_NOT_FOUND);
    }
}
