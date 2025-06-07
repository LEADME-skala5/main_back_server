package com.example.main_server.evaluation.common.repository;

import com.example.main_server.evaluation.common.entity.TaskParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskParticipationRepository extends JpaRepository<TaskParticipation, Long> {
}