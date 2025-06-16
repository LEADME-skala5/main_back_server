package com.example.main_server.evaluation.quantitative.repository;

import com.example.main_server.evaluation.quantitative.entity.WeeklyEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeeklyEvaluationRepository extends JpaRepository<WeeklyEvaluation, Long> {
}
