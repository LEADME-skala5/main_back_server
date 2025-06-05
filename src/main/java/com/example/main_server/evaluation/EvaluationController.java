package com.example.main_server.evaluation;

import com.example.main_server.evaluation.dto.EvaluationCriteriaRequestDTO;
import com.example.main_server.evaluation.dto.EvaluationKeywordsRequestDTO;
import com.example.main_server.evaluation.dto.EvaluationKeywordsResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/evaluation-criteria")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @PostMapping("/generate")
    public ResponseEntity<EvaluationKeywordsResponseDTO> setEvaluationCriteria(
            @RequestBody EvaluationCriteriaRequestDTO body) {
        EvaluationKeywordsResponseDTO response = evaluationService.generateKeywords(body);

        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    public ResponseEntity<String> setEvaluationCriteria(
            @RequestBody EvaluationKeywordsRequestDTO body) {
        evaluationService.saveKeywords(body.organizationId(), body.keywords());

        return ResponseEntity.ok("저장했음!");
    }
}
