package com.example.main_server.evaluation.common.repository;

import com.example.main_server.evaluation.common.entity.TaskParticipation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskParticipationRepository extends JpaRepository<TaskParticipation, Long> {

    // userId로 해당 사용자가 참여한 모든 TaskParticipation 조회
    List<TaskParticipation> findByUserId(Long userId);

    // 특정 taskId에 참여한 모든 TaskParticipation 조회 (특정 사용자 제외)
    @Query("SELECT tp FROM TaskParticipation tp WHERE tp.task.id = :taskId AND tp.user.id != :excludeUserId")
    List<TaskParticipation> findByTaskIdAndUserIdNot(@Param("taskId") Long taskId,
                                                     @Param("excludeUserId") Long excludeUserId);
}