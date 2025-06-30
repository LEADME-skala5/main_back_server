package com.example.main_server.report;

import com.example.main_server.report.dto.ReportSummaryResponse;
import com.example.main_server.report.dto.ReportsResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Sort;
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
    private final MongoTemplate mongoTemplate;

    public Object getReportById(String documentId) {
        Query query = new Query(Criteria.where("_id").is(documentId));
        Document rawDoc = mongoTemplate.findOne(query, Document.class, "reports");

        if (rawDoc == null) {
            throw new IllegalArgumentException("문서를 찾을 수 없습니다: " + documentId);
        }

        return rawDoc;
    }

    public ReportsResponse getReportsForLeader(Long userId, Long organizationId) {
        // 1. 개인 리포트
        Query personalQuery = new Query(Criteria.where("user.userId").is(userId)
                .and("type").in(List.of(TYPE_PERSONAL_QUARTER, TYPE_PERSONAL_ANNUAL)))
                .with(Sort.by(Sort.Order.desc("evaluatedYear"), Sort.Order.desc("evaluatedQuarter")));

        List<Document> personalDocs = mongoTemplate.find(personalQuery, Document.class, "reports");

        // 2. 팀 리포트
        Query teamQuery = new Query(Criteria.where("organizationId").is(organizationId)
                .and("type").in(List.of(TYPE_TEAM_QUARTER, TYPE_TEAM_ANNUAL)))
                .with(Sort.by(Sort.Order.desc("evaluatedYear"), Sort.Order.desc("evaluatedQuarter")));

        List<Document> teamDocs = mongoTemplate.find(teamQuery, Document.class, "reports");

        List<ReportSummaryResponse> personalReportResponses = personalDocs.stream()
                .map(this::convertToReportResponse)
                .toList();

        List<ReportSummaryResponse> teamReportResponses = teamDocs.stream()
                .map(this::convertToReportResponse)
                .toList();

        return new ReportsResponse(personalReportResponses, teamReportResponses);
    }

    public ReportsResponse getReportsForUser(Long userId) {
        Query query = new Query(Criteria.where("user.userId").is(userId)
                .and("type").in(List.of(TYPE_PERSONAL_QUARTER, TYPE_PERSONAL_ANNUAL)));

        // 정렬: evaluatedYear 내림차순, evaluatedQuarter 내림차순
        query.with(Sort.by(Sort.Order.desc("evaluatedYear"), Sort.Order.desc("evaluatedQuarter")));

        List<Document> personalDocs = mongoTemplate.find(query, Document.class, "reports");

        List<ReportSummaryResponse> personalReportResponses = personalDocs.stream()
                .map(this::convertToReportResponse) // rawDoc → DTO
                .toList();

        return new ReportsResponse(personalReportResponses, List.of());
    }

    private ReportSummaryResponse convertToReportResponse(Document rawDoc) {
        Document userDoc = rawDoc.get("user", Document.class); // 중첩 Document 추출
        ReportSummaryResponse.UserInfo userInfo = null;

        if (userDoc != null) {
            Long userId = userDoc.get("userId") != null ? ((Number) userDoc.get("userId")).longValue() : null;

            userInfo = new ReportSummaryResponse.UserInfo(
                    userId,
                    userDoc.getString("name"),
                    userDoc.getString("department")
            );
        }

        return new ReportSummaryResponse(
                rawDoc.getObjectId("_id").toHexString(),
                rawDoc.getString("type"),
                rawDoc.getInteger("evaluatedYear"),
                rawDoc.getInteger("evaluatedQuarter"),
                rawDoc.getString("createdAt"),
                rawDoc.getString("title"),
                rawDoc.getString("startDate"),
                rawDoc.getString("endDate"),
                userInfo
        );
    }
}
