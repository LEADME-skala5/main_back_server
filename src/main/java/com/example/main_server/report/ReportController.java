package com.example.main_server.report;

import com.example.main_server.common.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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
    public void getReports(@RequestParam Long userId, Authentication authentication
    ) {
        User currentUser = (User) authentication.getPrincipal();

        // ROLE_ORGANIZATION_LEADER
        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ORGANIZATION_LEADER"))) {

            reportService.getReportsForLeader(userId, currentUser.getOrganization().getId());
        }

        // ROLE_USER
        if (authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER"))) {

            reportService.getReportsForUser(userId);
        }
    }

    @GetMapping("/{documentId}")
    public void getReport(@PathVariable Long documentId) {
        reportService.getReport(documentId);
    }

}
