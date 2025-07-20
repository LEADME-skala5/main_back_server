package com.sk.skala.skore.user.entity;

import com.sk.skala.skore.base.BaseEntity;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
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

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;

    @Enumerated(EnumType.STRING)
    @Column(name = "career_level", nullable = false)
    private CareerLevel careerLevel;

    @Column(name = "is_manager")
    private Boolean isManager;

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

    public static User createUser(String name, String employeeNumber, String password,
                                  String teamsEmail, String slackEmail, String localPath,
                                  Organization organization, Department department, Division division,
                                  Boolean isManager, CareerLevel careerLevel) {
        return User.builder()
                .name(name)
                .employeeNumber(employeeNumber)
                .password(password)
                .teamsEmail(teamsEmail)
                .slackEmail(slackEmail)
                .localPath(localPath)
                .organization(organization)
                .department(department)
                .division(division)
                .isManager(isManager)
                .careerLevel(careerLevel)
                .userRole(UserRole.USER)
                .jobYears(0)
                .build();
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateProfile(String name, String slackEmail, String teamsEmail, String localPath) {
        this.name = name;
        this.slackEmail = slackEmail;
        this.teamsEmail = teamsEmail;
        this.localPath = localPath;
    }

    public void assignToOrganization(Organization organization) {
        this.organization = organization;
    }

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
