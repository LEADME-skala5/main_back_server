package com.sk.skala.skore.evaluation.peer.repository;

import com.sk.skala.skore.evaluation.peer.entity.EvaluationKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationKeywordRepository extends JpaRepository<EvaluationKeyword, Long> {
}
