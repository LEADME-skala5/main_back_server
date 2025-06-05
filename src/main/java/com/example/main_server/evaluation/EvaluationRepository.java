package com.example.main_server.evaluation;

import com.example.main_server.evaluation.entity.QualitativeEvaluationCriteria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<QualitativeEvaluationCriteria, Long> {
}
