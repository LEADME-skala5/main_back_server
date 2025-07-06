package com.example.main_server.evaluation.quantitative.dto;

import java.util.List;

public record QuarterOverviewResponse(
        boolean evaluated,
        int year,
        int quarter,
        List<UserOverviewResponse> users,
        QuarterEvaluationSummary summary) {
}
