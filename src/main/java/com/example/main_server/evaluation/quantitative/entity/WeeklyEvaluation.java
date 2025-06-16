package com.example.main_server.evaluation.quantitative.entity;

import com.example.main_server.common.entity.BaseEntity;
import com.example.main_server.common.entity.User;
import com.example.main_server.evaluation.common.entity.Task;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyEvaluation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluator_user_id", nullable = false)
    private User evaluatorUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluatee_user_id", nullable = false)
    private User evaluateeUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('탁월', '우수', '보통', '미흡', '부족')")
    private WeeklyEvaluationGrade grade;
}
