package com.example.main_server.evaluation.dto;

import java.util.List;

public record EvaluationKeywordsRequestDTO(
        Long organizationId,
        List<String> keywords
) {
}
