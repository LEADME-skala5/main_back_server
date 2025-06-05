package com.example.main_server.evaluation.dto;

import java.util.List;

public record EvaluationKeywordsResponseDTO(boolean success, List<String> keywords) {
}
