package com.example.main_server.activityLog.slack.chat.entity;

import com.example.main_server.activityLog.slack.team.entity.SlackTeam;
import com.example.main_server.auth.user.entity.User;
import com.example.main_server.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "slack_files")
public class SlackFile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "file_id")
    private String fileId;

    @Column(name = "message_ts")
    private String messageTs;

    @Column(name = "file_title")
    private String fileTitle;

    @Column(name = "mimetype")
    private String mimetype;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "url_private")
    private String urlPrivate;

    @Column(name = "permalink")
    private String permalink;

    @Column(name = "client_msg_id")
    private String clientMsgId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private SlackTeam slackTeam;
}
