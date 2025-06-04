package com.example.main_server.slack.chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "slack_chat_messages")
public class SlackMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "team_id")
    private Long team_id;

    @Column(name = "message_timestamp")
    private String message_timestamp;

    @Column(name = "content")
    private String content;

    @Column(name = "collected_at")
    private Timestamp collected_at;

}
