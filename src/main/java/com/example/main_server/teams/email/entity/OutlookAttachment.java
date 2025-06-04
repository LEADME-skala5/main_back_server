package com.example.main_server.teams.email.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "outlook_email_attachments")
@Getter
@Setter
public class OutlookAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_id", nullable = false)
    private OutlookEmail email;

    @Column(length = 500)
    private String name;

    @Column(name = "content_type", length = 100)
    private String contentType;

    private int size;

    @Column(name = "is_inline")
    private boolean isInline;
}
