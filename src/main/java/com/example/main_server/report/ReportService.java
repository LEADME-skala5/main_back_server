package com.example.main_server.report;

import com.example.main_server.report.dto.ReportSummaryResponse;
import com.example.main_server.report.dto.ReportsResponse;
import com.example.main_server.report.entity.BaseReport;
import com.example.main_server.report.repository.BaseRepostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {
    private static final String TYPE_TEAM_QUARTER = "team-quarter";
    private static final String TYPE_TEAM_ANNUAL = "team-annual";
    private static final String TYPE_PERSONAL_QUARTER = "personal-quarter";
    private static final String TYPE_PERSONAL_ANNUAL = "personal-annual";
    private final BaseRepostRepository baseRepostRepository;
    private final MongoTemplate mongoTemplate;

    private static void sortReports(List<BaseReport> personalReports) {
        personalReports.sort((r1, r2) -> {
            // 1. 년도 비교 (내림차순)
            int yearCompare = Integer.compare(r2.getEvaluatedYear(), r1.getEvaluatedYear());
            if (yearCompare != 0) {
                return yearCompare;
            }

            // 2. 같은 년도 내에서 분기 비교
            Integer q1 = r1.getEvaluatedQuarter();
            Integer q2 = r2.getEvaluatedQuarter();

            if (q1 == null && q2 == null) {
                return 0;
            }
            if (q1 == null) {
                return -1;
            }
            if (q2 == null) {
                return 1;
            }

            // 분기끼리는 내림차순 (4,3,2,1)
            return Integer.compare(q2, q1);
        });
    }

    public Object getReportById(String documentId) {
        Query query = new Query(Criteria.where("_id").is(documentId));
        Document rawDoc = mongoTemplate.findOne(query, Document.class, "reports");

        if (rawDoc == null) {
            throw new IllegalArgumentException("문서를 찾을 수 없습니다: " + documentId);
        }

        return rawDoc;
    }

    public ReportsResponse getReportsForLeader(Long userId, Long organizationId) {
        List<BaseReport> personalReports = baseRepostRepository.findAllByUser_UserIdAndTypeIn(userId,
                List.of(TYPE_PERSONAL_QUARTER, TYPE_PERSONAL_ANNUAL));

        List<BaseReport> teamReports = baseRepostRepository.findAllByUser_UserIdAndTypeIn(userId,
                List.of(TYPE_PERSONAL_QUARTER, TYPE_PERSONAL_ANNUAL));

        sortReports(personalReports);
        sortReports(teamReports);

        List<ReportSummaryResponse> personalReportResponses = personalReports.stream()
                .map(this::convertToReportResponse)
                .toList();

        List<ReportSummaryResponse> teamReportResponses = teamReports.stream()
                .map(this::convertToReportResponse)
                .toList();

        return new ReportsResponse(personalReportResponses, teamReportResponses);
    }

    public ReportsResponse getReportsForUser(Long userId) {
        List<BaseReport> personalReports = baseRepostRepository.findAllByUser_UserIdAndTypeIn(userId,
                List.of(TYPE_PERSONAL_QUARTER, TYPE_PERSONAL_ANNUAL));

        // 최신 기준으로 정렬
        sortReports(personalReports);

        List<ReportSummaryResponse> personalReportResponses = personalReports.stream()
                .map(this::convertToReportResponse)
                .toList();

        return new ReportsResponse(personalReportResponses, List.of());
    }

    private ReportSummaryResponse convertToReportResponse(BaseReport baseReport) {
//        ReportSummaryResponse.UserInfo userInfo = null;
//        if (baseReport.getUser() != null) {
//            userInfo = new ReportSummaryResponse.UserInfo(
//                    baseReport.getUser().getUserId(),
//                    baseReport.getUser().getName(),
//                    baseReport.getUser().getDepartment()
//            );
//        }

        return new ReportSummaryResponse(
                baseReport.getId(),
                baseReport.getType(),
                baseReport.getEvaluatedYear(),
                baseReport.getEvaluatedQuarter(),
                baseReport.getCreatedAt(),
                baseReport.getTitle(),
                baseReport.getStartDate(),
                baseReport.getEndDate()
        );
    }

}
