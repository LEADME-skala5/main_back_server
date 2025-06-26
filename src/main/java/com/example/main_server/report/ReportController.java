package com.example.main_server.report;

import com.example.main_server.common.entity.User;
import com.example.main_server.report.dto.ReportsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<ReportsResponse> getReports(@PathVariable Long userId, Authentication authentication
    ) {
        User currentUser = (User) authentication.getPrincipal();

        // ROLE_ORGANIZATION_LEADER
        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ORGANIZATION_LEADER"))) {

            ReportsResponse reports = reportService.getReportsForLeader(userId, currentUser.getOrganization().getId());
            return ResponseEntity.ok(reports);
        }

        // ROLE_USER
        ReportsResponse personalReports = reportService.getReportsForUser(userId);
        return ResponseEntity.ok(personalReports);
    }

    @GetMapping("reports/{documentId}")
    public void getReport(@PathVariable String documentId) {
        reportService.getReport(documentId);
    }
}
