package com.example.main_server.teams.email.entity;

import com.example.main_server.common.entity.User;
import com.example.main_server.common.entity.ExternalUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "outlook_emails")
@Getter
@Setter
public class OutlookEmail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email_id", unique = true, length = 255)
    private String emailId;

    @Column(length = 500)
    private String subject;

    @Lob
    @Column(name = "body_preview")
    private String bodyPreview;

    @Column(name = "body_content_type", length = 50)
    private String bodyContentType;

    @Lob
    @Column(name = "body_content")
    private String bodyContent;

    @Column(name = "is_internal_sender")
    private boolean isInternalSender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_user_id", foreignKey = @ForeignKey(name = "fk_email_sender_user"))
    private User senderUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_external_user_id", foreignKey = @ForeignKey(name = "fk_email_sender_external_user"))
    private ExternalUser senderExternalUser;

    @Column(name = "sent_datetime")
    private LocalDateTime sentDatetime;

    @Column(name = "received_datetime")
    private LocalDateTime receivedDatetime;

    @Column(name = "is_read")
    private boolean isRead;

    @Column(name = "is_draft")
    private boolean isDraft;

    @Column(name = "has_attachments")
    private boolean hasAttachments;

    @Column(length = 50)
    private String importance;

    @Lob
    @Column(name = "web_link")
    private String webLink;

    @OneToMany(mappedBy = "email", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OutlookAttachment> attachments = new ArrayList<>();

    @OneToMany(mappedBy = "email", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OutlookRecipient> recipients = new ArrayList<>();
}
