package com.example.main_server.evaluation.quantitative.dto;

public record TaskEvaluation(
        Long taskId,
        WeeklyEvaluationGrade grade
) {
}
