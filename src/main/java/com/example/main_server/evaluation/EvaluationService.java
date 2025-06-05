package com.example.main_server.evaluation;

import com.example.main_server.evaluation.dto.EvaluationCriteriaRequestDTO;
import com.example.main_server.evaluation.dto.EvaluationKeywordsDTO;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class EvaluationService {
    private static final String MODEL_SERVER_URL = "https://llm-api.example.com";

    private final RestClient restClient = RestClient.builder()
            .baseUrl(MODEL_SERVER_URL)
            .build();

    public void saveKeywords(Long teamId, List<String> keywords) {
        // TODO: jpaRepository에 저장로직 구현해야함
    }

    public EvaluationKeywordsDTO generateKeywords(EvaluationCriteriaRequestDTO body) {
        // TODO: AI Agent에 요청로직 구현해야함

        try {
            return restClient.post()
                    .uri("/aaa")
                    .body(body)
                    .retrieve()
                    .body(EvaluationKeywordsDTO.class);
        } catch (RestClientException e) {
            throw new RuntimeException("LLM API 요청 실패: " + e.getMessage(), e);
        }
    }
}
