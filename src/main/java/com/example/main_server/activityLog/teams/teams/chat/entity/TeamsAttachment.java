package com.example.main_server.activityLog.teams.teams.chat.entity;

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
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "teams_attachments")
@Getter
@Setter
public class TeamsAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teams_chat_id")
    private TeamsMessage teamsMessage;

    @Column(name = "attachment_id")
    private String attachmentId;

    @Lob
    @Column(name = "name")
    private String name;

    @Lob
    @Column(name = "content_url")
    private String contentUrl;
}
