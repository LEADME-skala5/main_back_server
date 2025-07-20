package com.sk.skala.skore.evaluation.peer.entity;

import com.sk.skala.skore.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "evaluation_keywords")
public class EvaluationKeyword extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "keyword", nullable = false, length = 100)
    private String keyword;

    @Column(name = "is_positive", nullable = false)
    private Boolean isPositive;

    @Column(name = "passionate_weight", nullable = false, precision = 3, scale = 2)
    private BigDecimal passionateWeight;

    @Column(name = "professional_weight", nullable = false, precision = 3, scale = 2)
    private BigDecimal professionalWeight;

    @Column(name = "proactive_weight", nullable = false, precision = 3, scale = 2)
    private BigDecimal proactiveWeight;

    @Column(name = "people_weight", nullable = false, precision = 3, scale = 2)
    private BigDecimal peopleWeight;

    @Column(name = "pessimistic_weight", nullable = false, precision = 3, scale = 2)
    private BigDecimal pessimisticWeight;

    @Column(name = "political_weight", nullable = false, precision = 3, scale = 2)
    private BigDecimal politicalWeight;

    @Column(name = "passive_weight", nullable = false, precision = 3, scale = 2)
    private BigDecimal passiveWeight;

    @Column(name = "personal_weight", nullable = false, precision = 3, scale = 2)
    private BigDecimal personalWeight;

    @OneToMany(mappedBy = "keyword")
    private List<PeerKeywordEvaluation> keywordEvaluations = new ArrayList<>();
}