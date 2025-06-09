package com.example.main_server.evaluation.common.repository;

import com.example.main_server.evaluation.common.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}