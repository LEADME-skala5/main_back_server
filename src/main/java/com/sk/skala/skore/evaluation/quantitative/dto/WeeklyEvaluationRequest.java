package com.sk.skala.skore.evaluation.quantitative.dto;

import java.util.List;

public record WeeklyEvaluationRequest(
        Long evaluatorUserId,
        Long evaluateeUserId,
        List<TaskEvaluation> evaluations
) {
}