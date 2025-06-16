package com.example.main_server.evaluation.quantitative;

import com.example.main_server.evaluation.quantitative.dto.WeeklyEvaluationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/quantitative-evaluation")
@RequiredArgsConstructor
public class QuantitativeEvaluationController {
    private final QuantitativeEvaluationService quantitativeEvaluationService;

    @PostMapping("/weekly")
    public void weeklyEvaluation(@RequestBody WeeklyEvaluationRequest request) {
        quantitativeEvaluationService.saveEvaluation(request);
    }
}
