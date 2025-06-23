package com.example.main_server.evaluation.quantitative.dto;

import java.time.LocalDate;

public record TaskResponse(
    Long id,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    int weight
) {
}
