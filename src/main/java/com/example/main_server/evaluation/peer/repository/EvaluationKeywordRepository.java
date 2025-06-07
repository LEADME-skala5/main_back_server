package com.example.main_server.evaluation.peer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.main_server.evaluation.peer.entity.EvaluationKeyword;

public interface EvaluationKeywordRepository extends JpaRepository<EvaluationKeyword, Long> {

}
