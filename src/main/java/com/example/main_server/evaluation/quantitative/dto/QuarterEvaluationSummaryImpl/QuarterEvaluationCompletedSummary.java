package com.example.main_server.evaluation.quantitative.dto.QuarterEvaluationSummaryImpl;

import com.example.main_server.evaluation.quantitative.dto.QuarterEvaluationSummary;

public record QuarterEvaluationCompletedSummary(
        int totalUsers,
        double averageScore,
        int ongoingProjectCount
) implements QuarterEvaluationSummary {
}
