package com.example.main_server.evaluation.quantitative;

import com.example.main_server.evaluation.quantitative.dto.QuarterOverviewResponse;
import com.example.main_server.evaluation.quantitative.dto.WeeklyEvaluationRequest;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private static final int YEAR = 2024;
    private static final int QUARTER = 1;
    private final QuantitativeEvaluationService quantitativeEvaluationService;

    @PostMapping("/weekly")
    public ResponseEntity<String> weeklyEvaluation(@RequestBody WeeklyEvaluationRequest request) {
        String message = quantitativeEvaluationService.saveEvaluation(request);
        URI location = URI.create("/weekly/");
        return ResponseEntity.created(location).body(message);
    }

    @GetMapping("/{organizationId}")
    public ResponseEntity<QuarterOverviewResponse> getEvaluations(@PathVariable Long organizationId) {
        QuarterOverviewResponse res = quantitativeEvaluationService.getEvaluations(organizationId, YEAR, QUARTER);
        return ResponseEntity.ok(res);
    }
}
