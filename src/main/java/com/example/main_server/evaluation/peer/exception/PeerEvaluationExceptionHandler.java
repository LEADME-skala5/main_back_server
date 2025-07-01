package com.example.main_server.evaluation.peer.exception;

import com.example.main_server.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice("com.example.main_server.evaluation.peer")
public class PeerEvaluationExceptionHandler {

    @ExceptionHandler(KeywordNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleKeywordNotFoundException(KeywordNotFoundException e) {
        log.warn("키워드 조회 실패: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("KEYWORD_NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidEvaluationRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEvaluationRequestException(InvalidEvaluationRequestException e) {
        log.warn("유효하지 않은 평가 요청: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("INVALID_REQUEST", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}