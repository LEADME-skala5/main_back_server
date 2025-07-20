package com.sk.skala.skore.evaluation.qualitative.dto;

import java.util.List;

public record QualitativeEvaluationKeywordsRequest(
        Long organizationId,
        List<String> keywords
) {
}
