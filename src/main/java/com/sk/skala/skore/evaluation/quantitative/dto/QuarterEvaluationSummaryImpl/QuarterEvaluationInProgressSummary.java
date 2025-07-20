package com.sk.skala.skore.evaluation.quantitative.dto.QuarterEvaluationSummaryImpl;

import com.sk.skala.skore.evaluation.quantitative.dto.QuarterEvaluationSummary;

public record QuarterEvaluationInProgressSummary(
        int totalTargetUsers,
        int evaluatedUserCount,
        double progressPercentage,
        int ongoingProjectCount
) implements QuarterEvaluationSummary {
}
