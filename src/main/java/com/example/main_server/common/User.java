package com.example.main_server.common;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "emails_user_id", unique = true)
    private String emailsUserId;

    @Column(name = "slack_user_id", unique = true)
    private String slackUserId;

    @Column(name = "teams_user_id", unique = true)
    private String teamsUserId;

    @Column(name = "onedrive_user_id", unique = true)
    private String onedriveUserId;

    @Column(name = "local_user_id", unique = true)
    private String localUserId;

    @Column(name = "name")
    private String name;

    @Column(name = "employee_number", unique = true)
    private String employeeNumber;

    @Column(name = "password")
    private String password;

    @Column(name = "primary_email")
    private String primaryEmail;

    @Column(name = "teams_email")
    private String teamsEmail;

    @Column(name = "slack_email")
    private String slackEmail;

    @Column(name = "local_path", columnDefinition = "TEXT")
    private String localPath;

    @Column(name = "department")
    private String department;

    @Column(name = "division")
    private String division;

    @Column(name = "organization")
    private String organization;

    @Column(name = "position")
    private String position;

    @Column(name = "career_level")
    private String careerLevel;

    @Column(name = "created_at")
    private Timestamp createdAt;
}
