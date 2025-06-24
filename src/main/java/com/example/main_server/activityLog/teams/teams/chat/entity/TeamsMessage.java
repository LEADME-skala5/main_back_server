package com.example.main_server.activityLog.teams.teams.chat.entity;

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
import java.time.Instant;
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
    @Column(name = "id")
    private Long id;

    @Column(name = "chat_id", nullable = false)
    private String chatId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_user_id")
    private User sender;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "collected_at")
    private Instant collectedAt;

    @OneToMany(mappedBy = "teamsMessage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TeamsAttachment> attachments = new ArrayList<>();
}
