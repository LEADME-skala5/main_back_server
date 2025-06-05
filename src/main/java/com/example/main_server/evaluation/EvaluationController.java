package com.example.main_server.evaluation;

import com.example.main_server.evaluation.dto.EvaluationCriteriaRequestDTO;
import com.example.main_server.evaluation.dto.EvaluationKeywordsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/evaluation-criteria")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @PostMapping
    public ResponseEntity<EvaluationKeywordsDTO> setEvaluationCriteria(@RequestBody EvaluationCriteriaRequestDTO body) {
        EvaluationKeywordsDTO response = evaluationService.generateKeywords(body);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{teamId}")
    public ResponseEntity<String> setEvaluationCriteria(
            @PathVariable("teamId") Long teamId,
            @RequestBody EvaluationKeywordsDTO body) {
        evaluationService.saveKeywords(teamId, body.keywords());

        return ResponseEntity.ok("Team ID: " + teamId + ", Content: ");
    }
}
