package com.sk.skala.skore.evaluation.qualitative;

import com.sk.skala.skore.evaluation.qualitative.dto.QualitativeEvaluationCriteriaRequest;
import com.sk.skala.skore.evaluation.qualitative.dto.QualitativeEvaluationKeywordsRequest;
import com.sk.skala.skore.evaluation.qualitative.dto.QualitativeEvaluationKeywordsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/qualitative-evaluation")
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
    @PreAuthorize("hasRole('ROLE_ORGANIZATION_LEADER')")
    public ResponseEntity<String> saveEvaluationCriteria(
            @RequestBody QualitativeEvaluationKeywordsRequest body) {
        qualitativeEvaluationService.saveKeywords(body.organizationId(), body.keywords());

        return ResponseEntity.ok("저장했음!");
    }
}
