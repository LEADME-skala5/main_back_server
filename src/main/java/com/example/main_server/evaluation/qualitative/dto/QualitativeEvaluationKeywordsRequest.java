package com.example.main_server.evaluation.qualitative.dto;

import java.util.List;

public record QualitativeEvaluationKeywordsRequest(
        Long organizationId,
        List<String> keywords
) {
}
