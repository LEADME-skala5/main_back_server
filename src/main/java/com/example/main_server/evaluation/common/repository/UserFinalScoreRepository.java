package com.example.main_server.evaluation.common.repository;

import com.example.main_server.evaluation.common.entity.UserFinalScore;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserFinalScoreRepository extends JpaRepository<UserFinalScore, Long> {
    @Query("SELECT fs FROM UserFinalScore fs WHERE fs.user.organization.id = :orgId AND fs.evaluationYear = :year AND fs.evaluationQuarter = :quarter")
    List<UserFinalScore> findByOrgAndPeriod(Long orgId, int year, int quarter);
}
