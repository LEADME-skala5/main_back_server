package com.example.main_server.auth.user.entity;

import com.example.main_server.common.entity.BaseEntity;
import com.example.main_server.common.entity.CareerLevel;
import com.example.main_server.common.entity.Department;
import com.example.main_server.common.entity.Division;
import com.example.main_server.common.entity.Job;
import com.example.main_server.common.entity.Organization;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "users")
public class User extends BaseEntity {
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

    @Column(name = "job_years", nullable = false)
    private int jobYears;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "career_level", nullable = false)
    private CareerLevel careerLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", foreignKey = @ForeignKey(name = "fk_user_organization"))
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "division_id", nullable = false)
    private Division division;

    public Division getDivision() {
        return organization != null ? organization.getDivision() : null;
    }

    public Department getDepartment() {
        Division division = getDivision();
        return division != null ? division.getDepartment() : null;
    }

    @Override
    public String toString() {
        return "User{id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
