package com.sk.skala.skore.evaluation.peer.exception;

import com.sk.skala.skore.base.BaseExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum PeerEvaluationExceptionType implements BaseExceptionType {

  INVALID_EVALUATION_REQUEST(BAD_REQUEST, "유효하지 않은 평가 요청입니다."),
  KEYWORD_NOT_FOUND(NOT_FOUND, "키워드를 찾을 수 없습니다.");

  private final HttpStatus httpStatus;
  private final String message;
}
