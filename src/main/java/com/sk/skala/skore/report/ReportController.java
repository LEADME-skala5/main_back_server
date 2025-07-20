package com.sk.skala.skore.report;

import com.sk.skala.skore.report.dto.ReportsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/users/{userId}/reports")
    public ResponseEntity<ReportsResponse> getReports(@PathVariable Long userId) {
        ReportsResponse reports = reportService.getReportsUserId(userId);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("reports/{documentId}")
    public ResponseEntity<Object> getReport(@PathVariable String documentId) {
        Object reportDetail = reportService.getReportById(documentId);
        return ResponseEntity.ok(reportDetail);
    }
}
