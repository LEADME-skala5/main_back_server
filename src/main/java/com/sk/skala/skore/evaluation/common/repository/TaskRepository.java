package com.sk.skala.skore.evaluation.common.repository;

import com.sk.skala.skore.evaluation.common.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}