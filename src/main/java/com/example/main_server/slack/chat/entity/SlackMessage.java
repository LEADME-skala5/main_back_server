package com.example.main_server.slack.chat.entity;

import com.example.main_server.common.entity.User;
import com.example.main_server.slack.team.entity.SlackTeam;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "slack_chat_messages")
public class SlackMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "message_timestamp")
    private String messageTimestamp;

    @Column(name = "content")
    private String content;

    @Column(name = "collected_at")
    private Timestamp collectedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private SlackTeam slackTeam;
}
