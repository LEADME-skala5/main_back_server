package com.example.main_server.teams.chat.entity;

import com.example.main_server.common.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "teams_messages")
@Getter
@Setter
public class TeamsMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "teams_user_id", nullable = false)
    private String teamsUserId;

    @Column(name = "teams_message_id", nullable = false)
    private String teamsMessageId;

    @Column(name = "teams_display_name")
    private String teamsDisplayName;

    @Column(name = "message_type")
    private String messageType;

    @Column(name = "body_content_type")
    private String bodyContentType;

    @Lob
    @Column(name = "body_content")
    private String bodyContent;

    @Column(name = "created_datetime")
    private Timestamp createdDatetime;

    @Column(name = "log_date")
    private LocalDate logDate;

    @Column(name = "message_datetime")
    private Timestamp messageDatetime;

    @Column(name = "collected_at")
    private Timestamp collectedAt = Timestamp.from(Instant.now());

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamsAttachment> attachments = new ArrayList<>();
}