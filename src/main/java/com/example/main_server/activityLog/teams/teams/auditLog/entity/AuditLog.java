package com.example.main_server.activityLog.teams.teams.auditLog.entity;

import com.example.main_server.auth.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "outlook_audit_logs")
@Getter
@Setter
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 100)
    private Long id;

    @Column(name = "creation_time")
    private LocalDateTime creationTime;

    @Column(length = 100)
    private String operation;

    @Column(length = 50)
    private String workload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_audit_user"))
    private User user;

    @Column(name = "application_display_name", length = 100)
    private String applicationDisplayName;

    @Lob
    @Column(name = "file_name")
    private String fileName;

    @Lob
    @Column(name = "site_url")
    private String siteUrl;

    @Column(name = "collected_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp collectedAt = new Timestamp(System.currentTimeMillis());
}
