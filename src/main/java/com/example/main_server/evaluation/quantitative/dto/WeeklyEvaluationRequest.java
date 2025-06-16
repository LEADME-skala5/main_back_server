package com.example.main_server.evaluation.quantitative.dto;

import java.util.List;

public record WeeklyEvaluationRequest(
        Long evaluatorUserId,
        Long evaluateeUserId,
        List<TaskEvaluation> evaluations
) {
}