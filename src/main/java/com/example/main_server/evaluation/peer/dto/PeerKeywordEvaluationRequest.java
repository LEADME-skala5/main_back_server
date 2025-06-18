package com.example.main_server.evaluation.peer.dto;

import java.util.List;

public record PeerKeywordEvaluationRequest(
        Long evaluatorUserId,
        List<Evaluation> evaluations
) {
    public record Evaluation(
            Long evaluateeUserId,
            List<Long> keywordIds
    ) {
    }
}

