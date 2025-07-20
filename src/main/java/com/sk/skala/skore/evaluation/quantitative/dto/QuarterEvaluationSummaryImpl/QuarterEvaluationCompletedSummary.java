package com.sk.skala.skore.evaluation.quantitative.dto.QuarterEvaluationSummaryImpl;

import com.sk.skala.skore.evaluation.quantitative.dto.QuarterEvaluationSummary;

public record QuarterEvaluationCompletedSummary(
        int totalUsers,
        double averageScore,
        int ongoingProjectCount
) implements QuarterEvaluationSummary {
}
