package com.example.main_server.evaluation.common.repository;

import com.example.main_server.evaluation.common.entity.Task;
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

    // 두 사용자가 함께 참여한 모든 Task 조회
    @Query("SELECT DISTINCT tp1.task FROM TaskParticipation tp1 " +
            "JOIN TaskParticipation tp2 ON tp1.task.id = tp2.task.id " +
            "WHERE tp1.user.id = :userId1 AND tp2.user.id = :userId2")
    List<Task> findCommonTasksByUserIds(@Param("userId1") Long userId1, @Param("userId2") Long userId2);
}
