package com.example.main_server.evaluation.qualitative;

import com.example.main_server.evaluation.qualitative.dto.QualitativeEvaluationCriteriaRequest;
import com.example.main_server.evaluation.qualitative.dto.QualitativeEvaluationKeywordsRequest;
import com.example.main_server.evaluation.qualitative.dto.QualitativeEvaluationKeywordsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/evaluation-criteria")
@RequiredArgsConstructor
public class QualitativeEvaluationController {

    private final QualitativeEvaluationService qualitativeEvaluationService;

    @PostMapping("/generate")
    public ResponseEntity<QualitativeEvaluationKeywordsResponse> generateEvaluationCriteria(
            @RequestBody QualitativeEvaluationCriteriaRequest body) {
        QualitativeEvaluationKeywordsResponse response = qualitativeEvaluationService.generateKeywords(body);

        return ResponseEntity.ok(response);
    }

    @PostMapping("")
    public ResponseEntity<String> saveEvaluationCriteria(
            @RequestBody QualitativeEvaluationKeywordsRequest body) {
        qualitativeEvaluationService.saveKeywords(body.organizationId(), body.keywords());

        return ResponseEntity.ok("저장했음!");
    }
}
