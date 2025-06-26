package com.example.main_server.report.dto;

import java.util.List;

public record ReportsResponse(
        List<ReportResponse> personalReports, List<ReportResponse> teamReports
) {
}
