package com.sk.skala.skore.report.dto;

import java.util.List;

public record ReportsResponse(
        List<ReportSummaryResponse> personalReports, List<ReportSummaryResponse> teamReports
) {
}
