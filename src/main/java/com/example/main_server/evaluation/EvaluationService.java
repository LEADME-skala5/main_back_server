package com.example.main_server.evaluation;

import com.example.main_server.common.entity.Organization;
import com.example.main_server.common.repository.OrganizationRepository;
import com.example.main_server.evaluation.dto.EvaluationCriteriaRequestDTO;
import com.example.main_server.evaluation.dto.EvaluationKeywordsResponseDTO;
import com.example.main_server.evaluation.entity.QualitativeEvaluationCriteria;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EvaluationService {
    private static final String MODEL_SERVER_URL = "https://llm-api.example.com";
    private final EvaluationRepository evaluationRepository;
    private final OrganizationRepository organizationRepository;
    private final RestClient restClient = RestClient.builder()
            .baseUrl(MODEL_SERVER_URL)
            .build();

    @Transactional
    public void saveKeywords(Long organizationId, List<String> keywords) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new EntityNotFoundException("조직 ID 없음: " + organizationId));

        List<QualitativeEvaluationCriteria> entities = keywords.stream()
                .map(keyword -> QualitativeEvaluationCriteria.builder()
                        .organization(organization)
                        .keyword(keyword)
                        .build())
                .toList();

        evaluationRepository.saveAll(entities);
    }

    public EvaluationKeywordsResponseDTO generateKeywords(EvaluationCriteriaRequestDTO body) {
        // TODO: AI Agent에 요청로직 구현해야함

        try {
            List<String> keywords = restClient.post()
                    .uri("/aaa")
                    .body(body)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });

            return new EvaluationKeywordsResponseDTO(true, keywords);
        } catch (RestClientException e) {
            log.error("LLM API 요청 실패: {}", e.getMessage(), e);

            return new EvaluationKeywordsResponseDTO(
                    false,
                    List.of("키워드 생성 실패")
            );
        }
    }
}
