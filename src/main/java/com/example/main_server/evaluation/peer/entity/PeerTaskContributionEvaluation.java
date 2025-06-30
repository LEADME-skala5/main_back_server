package com.example.main_server.evaluation.peer.entity;

import com.example.main_server.auth.user.entity.User;
import com.example.main_server.common.entity.BaseEntity;
import com.example.main_server.evaluation.common.entity.Task;
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
@Table(name = "peer_task_contribution_evaluations")
public class PeerTaskContributionEvaluation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "task_id", foreignKey = @ForeignKey(name = "fk_task_contribution_evaluation_task"))
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "evaluator_user_id", foreignKey = @ForeignKey(name = "fk_task_contribution_evaluation_evaluator"))
    private User evaluator;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "target_user_id", foreignKey = @ForeignKey(name = "fk_task_contribution_evaluation_target"))
    private User target;

    @Column(name = "score", nullable = false, columnDefinition = "TINYINT UNSIGNED")
    private Integer score;
}
