package com.example.main_server.evaluation.qualitative.dto;

import java.util.List;

public record QualitativeEvaluationKeywordsResponse(boolean success, List<String> keywords) {
}
