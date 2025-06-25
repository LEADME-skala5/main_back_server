package com.example.main_server.report;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    public void getReport(Long documentId) {
        // TODO: 리포트 상세 조회
    }

    public void getReportsForLeader(Long userId, Long organizationId) {
    }

    public void getReportsForUser(Long userId) {
        // user.userId와 type =="team-quarter" 조회
    }
}
