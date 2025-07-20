package com.sk.skala.skore.activityLog.teams.email.entity;

import com.sk.skala.skore.user.entity.ExternalUser;
import com.sk.skala.skore.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "outlook_email_recipients")
@Getter
@Setter
public class OutlookRecipient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_id", nullable = false)
    private OutlookEmail email;

    @Column(name = "is_internal_user")
    private boolean isInternalUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_recipient_user"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "external_user_id", foreignKey = @ForeignKey(name = "fk_recipient_external_user"))
    private ExternalUser externalUser;

    @Column(name = "recipient_name", length = 255)
    private String recipientName;

    @Column(name = "recipient_address", length = 255)
    private String recipientAddress;
}
