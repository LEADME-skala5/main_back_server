package com.sk.skala.skore.report.dto;

public record ReportSummaryResponse(
        String id,
        String type,
        Integer evaluatedYear,
        Integer evaluatedQuarter,
        String createdAt,
        String title,
        String startDate,
        String endDate,
        UserInfo user
) {
    public record UserInfo(
            Long userId,
            String name,
            String department
    ) {
    }
}

