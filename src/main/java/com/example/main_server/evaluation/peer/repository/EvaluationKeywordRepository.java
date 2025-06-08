package com.example.main_server.evaluation.peer.repository;

import com.example.main_server.evaluation.peer.entity.EvaluationKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluationKeywordRepository extends JpaRepository<EvaluationKeyword, Long> {
}
