package com.example.main_server.evaluation.qualitative;

import com.example.main_server.evaluation.qualitative.entity.QualitativeEvaluationCriteria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualitativeEvaluationRepository extends JpaRepository<QualitativeEvaluationCriteria, Long> {
}
