package com.sk.skala.skore.evaluation.peer.exception;

import com.sk.skala.skore.base.BaseException;
import com.sk.skala.skore.base.BaseExceptionType;

public class PeerEvaluationException extends BaseException {

  public PeerEvaluationException(final PeerEvaluationExceptionType exceptionType) {
    super(exceptionType);
  }
}
