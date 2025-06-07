package com.example.main_server.evaluation.peer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.main_server.evaluation.peer.entity.PeerKeywordEvaluation;

public interface PeerKeywordEvaluationRepository extends JpaRepository<PeerKeywordEvaluation, Long> {

}
