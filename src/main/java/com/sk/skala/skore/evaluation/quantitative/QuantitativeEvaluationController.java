package com.sk.skala.skore.evaluation.quantitative;

import com.sk.skala.skore.evaluation.quantitative.dto.QuarterOverviewResponse;
import com.sk.skala.skore.evaluation.quantitative.dto.WeeklyEvaluationRequest;
import com.sk.skala.skore.util.EvaluationPeriodService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/quantitative-evaluation")
@RequiredArgsConstructor
public class QuantitativeEvaluationController {
    private final EvaluationPeriodService evaluationPeriodService;
    private final QuantitativeEvaluationService quantitativeEvaluationService;

    @PostMapping("/weekly")
    @PreAuthorize("hasRole('ROLE_ORGANIZATION_LEADER')")
    public ResponseEntity<String> saveWeeklyEvaluation(@RequestBody WeeklyEvaluationRequest request) {
        String message = quantitativeEvaluationService.saveEvaluation(request);
        URI location = URI.create("/weekly/");
        return ResponseEntity.created(location).body(message);
    }

    @GetMapping("/weekly/{userId}")
    @PreAuthorize("hasRole('ROLE_ORGANIZATION_LEADER')")
    public ResponseEntity<Object> getWeeklyAISummary(@PathVariable Long userId) {
        Object res = quantitativeEvaluationService.getWeeklyAISummary(userId,
                evaluationPeriodService.getCurrentYear(), evaluationPeriodService.getCurrentQuarter());
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{organizationId}")
    @PreAuthorize("hasRole('ROLE_ORGANIZATION_LEADER')")
    public ResponseEntity<QuarterOverviewResponse> getEvaluations(@PathVariable Long organizationId) {
        QuarterOverviewResponse res = quantitativeEvaluationService.getEvaluations(organizationId,
                evaluationPeriodService.getCurrentYear(), evaluationPeriodService.getCurrentQuarter());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/weekly/generate")
    @PreAuthorize("hasRole('ROLE_ORGANIZATION_LEADER')")
    public ResponseEntity<String> generateWeeklyEvaluation(@RequestBody WeeklyEvaluationRequest request) {
        String message = quantitativeEvaluationService.generateEvaluation(request);
        URI location = URI.create("/weekly/generate");
        return ResponseEntity.created(location).body(message);
    }
}
