package com.sk.skala.skore.evaluation.quantitative.entity;

import com.sk.skala.skore.auth.user.entity.User;
import com.sk.skala.skore.base.BaseEntity;
import com.sk.skala.skore.evaluation.common.entity.Task;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "weekly_evaluations")
public class WeeklyEvaluation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "evaluation_year", nullable = false)
    private int evaluationYear;

    @Column(name = "evaluation_quarter", nullable = false)
    private int evaluationQuarter;

    @Column(name = "grade", nullable = false)
    private int grade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluator_user_id", nullable = false)
    private User evaluatorUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluatee_user_id", nullable = false)
    private User evaluateeUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;
}
