package com.example.main_server.evaluation.quantitative.dto.QuarterEvaluationSummaryImpl;

import com.example.main_server.evaluation.quantitative.dto.QuarterEvaluationSummary;

public record QuarterEvaluationInProgressSummary(
        int totalTargetUsers,
        int evaluatedUserCount,
        double progressPercentage,
        int ongoingProjectCount
) implements QuarterEvaluationSummary {
}
