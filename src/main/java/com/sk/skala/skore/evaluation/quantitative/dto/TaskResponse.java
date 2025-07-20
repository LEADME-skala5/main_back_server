package com.sk.skala.skore.evaluation.quantitative.dto;

import java.time.LocalDate;

public record TaskResponse(
        Long taskId,
        String name,
        LocalDate startDate,
        LocalDate endDate,
        boolean isEvaluated,
        Integer grade
) {
}
