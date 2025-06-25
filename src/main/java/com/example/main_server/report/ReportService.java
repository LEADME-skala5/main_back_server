package com.example.main_server.report;

import com.example.main_server.report.repository.PersonalQuarterRepostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final PersonalQuarterRepostRepository personalQuarterRepostRepository;

    public void getReport(Long documentId) {
        // TODO: 리포트 상세 조회
    }

    public void getReportsForLeader(Long userId, Long organizationId) {
    }

    public void getReportsForUser(Long userId) {
    }
}
