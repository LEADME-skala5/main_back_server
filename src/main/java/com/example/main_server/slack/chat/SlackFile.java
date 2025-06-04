package com.example.main_server.slack.chat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "slack_files")
public class SlackFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "file_id")
    private String file_id;

    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "team_id")
    private Long team_id;

    @Column(name = "message_ts")
    private String message_ts;

    @Column(name = "file_title")
    private String file_title;

    @Column(name = "mimetype")
    private String mimetype;

    @Column(name = "created_at")
    private Long created_at;

    @Column(name = "url_private")
    private String url_private;

    @Column(name = "permalink")
    private String permalink;

    @Column(name = "client_msg_id")
    private String client_msg_id;

    @Column(name = "collected_at")
    private Timestamp collected_at;

}
