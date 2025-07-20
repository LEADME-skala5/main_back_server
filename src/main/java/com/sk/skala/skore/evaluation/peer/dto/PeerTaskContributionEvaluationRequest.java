package com.sk.skala.skore.evaluation.peer.dto;

public record PeerTaskContributionEvaluationRequest(
        Long taskId,
        Long evaluatorUserId,
        Long targetUserId,
        Integer score
) {
}
