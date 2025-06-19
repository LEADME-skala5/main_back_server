package com.example.main_server.evaluation.common.entity;

import com.example.main_server.common.entity.BaseEntity;
import com.example.main_server.common.entity.Organization;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tasks")
public class Task extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", foreignKey = @ForeignKey(name = "fk_task_organization"))
    private Organization organization;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "weight", nullable = false)
    private int weight;

    @OneToMany(mappedBy = "task")
    private List<TaskParticipation> taskParticipations = new ArrayList<>();
}
