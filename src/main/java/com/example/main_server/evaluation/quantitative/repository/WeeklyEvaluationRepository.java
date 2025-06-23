package com.example.main_server.evaluation.quantitative.repository;

import com.example.main_server.evaluation.quantitative.entity.WeeklyEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WeeklyEvaluationRepository extends JpaRepository<WeeklyEvaluation, Long> {
    // 아직 평가되지 않은 구성원이 한 명이라도 있으면 true
    @Query("""
                select count(u) > 0
                from User u
                where u.organization.id = :orgId
                  and u.id not in (
                      select we.evaluateeUser.id
                      from WeeklyEvaluation we
                      where we.evaluationYear = :year
                        and we.evaluationQuarter = :quarter
                  )
            """)
    boolean existsUnEvaluatedUsers(Long orgId, int year, int quarter);

    
}
