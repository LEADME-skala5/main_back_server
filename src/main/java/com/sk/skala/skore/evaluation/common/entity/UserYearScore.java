package com.sk.skala.skore.evaluation.common.entity;

import com.sk.skala.skore.base.BaseEntity;
import com.sk.skala.skore.user.entity.User;
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
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_year_scores")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserYearScore extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_final_scores_user"))
    private User user;

    @NotNull
    @Column(name = "evaluation_year")
    private Integer evaluationYear;

    @Digits(integer = 1, fraction = 2)
    @Column(name = "final_score", precision = 3, scale = 2)
    private BigDecimal finalScore;

    @Column(name = "user_rank")
    private Integer userRank;

    @Column(name = "team_rank")
    private Integer teamRank;
}
