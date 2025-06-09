package com.example.main_server.evaluation.peer.dto;

import java.util.List;

public record PeerKeywordEvaluationRequest(
        Long evaluatorUserId,
        Long evaluateeUserId,
        List<Long> keywordIds) {
}
