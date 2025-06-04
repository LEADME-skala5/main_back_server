package com.example.main_server.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "external_users")
public class ExternalUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "teams_user_id")
    private String teams_user_id;

    @Column(name = "display_name")
    private String display_name;

    @Column(name = "source")
    private String source;

    @Column(name = "created_at")
    private Timestamp created_at;

}