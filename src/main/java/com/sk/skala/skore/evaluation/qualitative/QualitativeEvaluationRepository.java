package com.sk.skala.skore.evaluation.qualitative;

import com.sk.skala.skore.evaluation.qualitative.entity.QualitativeEvaluationCriteria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualitativeEvaluationRepository extends JpaRepository<QualitativeEvaluationCriteria, Long> {
}
