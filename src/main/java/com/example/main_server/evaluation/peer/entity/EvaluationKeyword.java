package com.example.main_server.evaluation.peer.entity;

import com.example.main_server.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

    @OneToMany(mappedBy = "keyword")
    private List<PeerKeywordEvaluation> keywordEvaluations = new ArrayList<>();
}