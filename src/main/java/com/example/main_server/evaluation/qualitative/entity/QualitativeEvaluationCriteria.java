package com.example.main_server.evaluation.qualitative.entity;

import com.example.main_server.common.entity.BaseEntity;
import com.example.main_server.common.entity.Organization;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "qualitative_evaluation_criteria")
@Data
@NoArgsConstructor
public class QualitativeEvaluationCriteria extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @Column(name = "keyword", nullable = false, length = 100)
    private String keyword;

    @Builder
    public QualitativeEvaluationCriteria(Organization organization, String keyword) {
        this.organization = organization;
        this.keyword = keyword;
    }
}
