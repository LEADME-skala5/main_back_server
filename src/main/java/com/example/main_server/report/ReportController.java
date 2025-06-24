package com.example.main_server.report;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping
    public void getReports(@RequestParam Long userId) {
        reportService.getReports(userId);
    }

    @GetMapping("/{documentId}")
    public void getReport(@PathVariable Long documentId) {
        reportService.getReport(documentId);
    }

}
