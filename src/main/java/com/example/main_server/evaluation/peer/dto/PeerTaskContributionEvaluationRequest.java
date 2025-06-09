package com.example.main_server.evaluation.peer.dto;

public record PeerTaskContributionEvaluationRequest(
        Long taskId,
        Long evaluatorUserId,
        Long targetUserId,
        Integer score
) {
}
