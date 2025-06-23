package com.example.main_server.evaluation.common.repository;

import com.example.main_server.evaluation.common.entity.UserQuarterScore;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserQuarterScoreRepository extends JpaRepository<UserQuarterScore, Long> {
    @Query("SELECT fs FROM UserQuarterScore fs WHERE fs.user.organization.id = :orgId AND fs.evaluationYear = :year AND fs.evaluationQuarter = :quarter")
    List<UserQuarterScore> findByOrgAndPeriod(Long orgId, int year, int quarter);
}
