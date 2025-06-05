package com.example.main_server.common.entity;

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

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slack_user_id", unique = true, length = 255)
    private String slackUserId;

    @Column(name = "teams_email", unique = true, length = 255)
    private String teamsEmail;

    @Column(name = "teams_user_id", unique = true, length = 255)
    private String teamsUserId;

    @Column(name = "onedrive_user_id", unique = true, length = 255)
    private String onedriveUserId;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "employee_number", unique = true, length = 50)
    private String employeeNumber;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "primary_email", length = 255)
    private String primaryEmail;

    @Column(name = "slack_email", length = 255)
    private String slackEmail;

    @Column(name = "local_path", columnDefinition = "TEXT")
    private String localPath;

    @Column(name = "is_manager", nullable = false, columnDefinition = "TINYINT(1)")
    private Boolean isManager = false;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", foreignKey = @ForeignKey(name = "fk_user_department"))
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "division_id", foreignKey = @ForeignKey(name = "fk_user_division"))
    private Division division;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", foreignKey = @ForeignKey(name = "fk_user_organization"))
    private Organization organization;

    @Column(name = "career_level", nullable = false, length = 50)
    private String careerLevel;

}
