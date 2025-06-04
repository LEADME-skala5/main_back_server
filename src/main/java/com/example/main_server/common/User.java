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
    @Column(name = "id")
    private Long id;

    @Column(name = "slack_user_id")
    private String slack_user_id;

    @Column(name = "teams_email")
    private String teams_email;

    @Column(name = "teams_user_id")
    private String teams_user_id;

    @Column(name = "onedrive_user_id")
    private String onedrive_user_id;

    @Column(name = "name")
    private String name;

    @Column(name = "employee_number")
    private String employee_number;

    @Column(name = "password")
    private String password;

    @Column(name = "primary_email")
    private String primary_email;

    @Column(name = "slack_email")
    private String slack_email;

    @Column(name = "local_path")
    private String local_path;

    @Column(name = "department")
    private String department;

    @Column(name = "division")
    private String division;

    @Column(name = "organization")
    private String organization;

    @Column(name = "position")
    private String position;

    @Column(name = "career_level")
    private String career_level;

}
