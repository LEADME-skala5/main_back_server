package com.example.main_server.evaluation.quantitative.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record UserOverviewResponse(
        Long userId,
        String name,
        String position,
        String email,
        String recentReportId,
        List<TaskResponse> tasks,
        BigDecimal quarterScore, // null = 미평가
        Integer rank,            // null = 미평가
        LocalDate lastUpdated,
        boolean isUserEvaluated) {
}