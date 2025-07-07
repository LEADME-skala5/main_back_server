package com.example.main_server.report;

import com.example.main_server.report.dto.ReportSummaryResponse;
import com.example.main_server.report.dto.ReportsResponse;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * 보고서 관련 기능을 제공하는 서비스 클래스입니다.
 * <p>
 * MongoDB에 저장된 보고서 데이터를 조회하고 관리하는 기능을 제공합니다. 개인 분기/연간 보고서와 팀 분기/연간 보고서를 처리합니다.
 * <p>
 * 주요 기능: - ID로 특정 보고서 조회 - 사용자 ID를 기준으로 관련된 모든 보고서 조회 - 보고서 유형별 분류 및 정렬 - 보고서 데이터 변환 및 가공
 */


@Service
@RequiredArgsConstructor
public class ReportService {
    private static final String TYPE_TEAM_QUARTER = "team-quarter";
    private static final String TYPE_TEAM_ANNUAL = "team-annual";
    private static final String TYPE_PERSONAL_QUARTER = "personal-quarter";
    private static final String TYPE_PERSONAL_ANNUAL = "personal-annual";
    private final MongoTemplate mongoTemplate;

    public Object getReportById(String documentId) {
        Query query = new Query(Criteria.where("_id").is(documentId));
        Document rawDoc = mongoTemplate.findOne(query, Document.class, "reports");

        if (rawDoc == null) {
            throw new IllegalArgumentException("문서를 찾을 수 없습니다: " + documentId);
        }

        return rawDoc;
    }

    public ReportsResponse getReportsUserId(Long userId) {
        List<ReportSummaryResponse> personalReportResponses =
                getReportsByType(userId, List.of(TYPE_PERSONAL_ANNUAL, TYPE_PERSONAL_QUARTER));

        List<ReportSummaryResponse> teamReportResponses =
                getReportsByType(userId, List.of(TYPE_TEAM_ANNUAL, TYPE_TEAM_QUARTER));

        return new ReportsResponse(personalReportResponses, teamReportResponses);
    }

    private List<ReportSummaryResponse> getReportsByType(Long userId, List<String> types) {
        Query query = new Query(Criteria.where("user.userId").is(userId)
                .and("type").in(types))
                .with(Sort.by(Sort.Order.desc("evaluated_year"), Sort.Order.desc("evaluated_quarter")));

        List<Document> documents = mongoTemplate.find(query, Document.class, "reports");

        return documents.stream()
                .map(this::convertToReportResponse)
                .sorted(Comparator.comparingInt(r -> getTypePriority(r.type())))
                .toList();
    }

    private ReportSummaryResponse convertToReportResponse(Document rawDoc) {
        Document userDoc = rawDoc.get("user", Document.class);
        ReportSummaryResponse.UserInfo userInfo = null;

        if (userDoc != null) {
            Long userId = userDoc.get("userId") != null ? ((Number) userDoc.get("userId")).longValue() : null;

            userInfo = new ReportSummaryResponse.UserInfo(
                    userId,
                    userDoc.getString("name"),
                    userDoc.getString("department")
            );
        }

        Object rawCreatedAt = rawDoc.get("created_at");
        String createdAtStr = null;

        if (rawCreatedAt instanceof Date) {
            createdAtStr = new SimpleDateFormat("yyyy-MM-dd").format((Date) rawCreatedAt);
        } else if (rawCreatedAt instanceof String) {
            createdAtStr = ((String) rawCreatedAt).split("T")[0];
        } else {
            createdAtStr = "-";
        }

        return new ReportSummaryResponse(
                rawDoc.getObjectId("_id").toHexString(),
                rawDoc.getString("type"),
                rawDoc.getInteger("evaluated_year"),
                rawDoc.getInteger("evaluated_quarter"),
                createdAtStr,
                rawDoc.getString("title"),
                rawDoc.getString("startDate"),
                rawDoc.getString("endDate"),
                userInfo
        );
    }

    private int getTypePriority(String type) {
        return switch (type) {
            case TYPE_PERSONAL_ANNUAL, TYPE_TEAM_ANNUAL -> 0;
            case TYPE_PERSONAL_QUARTER, TYPE_TEAM_QUARTER -> 1;
            default -> 2;
        };
    }
}
