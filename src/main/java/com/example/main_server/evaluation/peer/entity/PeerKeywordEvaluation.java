package com.example.main_server.evaluation.peer.entity;

import com.example.main_server.auth.user.entity.User;
import com.example.main_server.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "peer_keyword_evaluations")
public class PeerKeywordEvaluation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "evaluation_year", nullable = false)
    private int evaluationYear;

    @Column(name = "evaluation_quarter", nullable = false)
    private int evaluationQuarter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "evaluator_user_id", foreignKey = @ForeignKey(name = "fk_keyword_evaluation_evaluator"))
    private User evaluator;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "evaluatee_user_id", foreignKey = @ForeignKey(name = "fk_keyword_evaluation_evaluatee"))
    private User evaluatee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "keyword_id", foreignKey = @ForeignKey(name = "fk_keyword_evaluation_keyword"))
    private EvaluationKeyword keyword;
}