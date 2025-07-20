package com.sk.skala.skore.evaluation.common.repository;

import com.sk.skala.skore.evaluation.common.entity.TaskParticipation;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskParticipationRepository extends JpaRepository<TaskParticipation, Long> {
    @Query("SELECT tp FROM TaskParticipation tp " +
            "JOIN FETCH tp.task t " +
            "JOIN FETCH tp.user u " +
            "WHERE tp.user.id = :userId")
    List<TaskParticipation> findByUserIdWithTaskAndUser(@Param("userId") Long userId);

    @Query("SELECT tp FROM TaskParticipation tp " +
            "JOIN FETCH tp.task t " +
            "JOIN FETCH tp.user u " +
            "WHERE tp.task.id IN :taskIds")
    List<TaskParticipation> findByTaskIdsWithTaskAndUser(@Param("taskIds") Set<Long> taskIds);


    @Query("SELECT tp FROM TaskParticipation tp JOIN FETCH tp.task WHERE tp.user.id IN :userIds")
    List<TaskParticipation> findCurrentTaskParticipationsByUserIds(@Param("userIds") List<Long> userIds);
}
