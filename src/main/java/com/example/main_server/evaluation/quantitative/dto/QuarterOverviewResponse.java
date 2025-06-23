package com.example.main_server.evaluation.quantitative.dto;

import java.util.List;

public record QuarterOverviewResponse(
        boolean evaluated,
        List<UserOverviewResponse> users) {
}
