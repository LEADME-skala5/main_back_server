package com.example.main_server.teams.chat.entity;

import com.example.main_server.common.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "teams_attachments")
@Getter
@Setter
public class TeamsAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private TeamsMessage message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "teams_user_id", nullable = false)
    private String teamsUserId;

    @Column(name = "teams_message_id", nullable = false)
    private String teamsMessageId;

    @Column(name = "teams_attachments_id")
    private String teamsAttachmentsId;

    @Column(name = "attachment_name")
    private String attachmentName;

    @Lob
    @Column(name = "content_url")
    private String contentUrl;

    @Column(name = "log_date")
    private LocalDate logDate;

    @Column(name = "collected_at")
    private Timestamp collectedAt = Timestamp.from(Instant.now());
}

