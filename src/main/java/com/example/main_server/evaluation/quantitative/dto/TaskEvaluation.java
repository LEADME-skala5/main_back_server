package com.example.main_server.evaluation.quantitative.dto;

import com.example.main_server.evaluation.quantitative.entity.WeeklyEvaluationGrade;

public record TaskEvaluation(
        Long taskId,
        WeeklyEvaluationGrade grade
) {
}
