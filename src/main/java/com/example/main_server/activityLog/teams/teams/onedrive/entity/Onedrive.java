package com.example.main_server.activityLog.teams.teams.onedrive.entity;

import com.example.main_server.common.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
        name = "teams_onedrive_file_logs",
        uniqueConstraints = @UniqueConstraint(name = "uk_file_version", columnNames = {"file_item_id", "version_id"})
)
@Getter
@Setter
public class Onedrive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_item_id", nullable = false, length = 255)
    private String fileItemId;

    @Column(name = "version_id", nullable = false, length = 50)
    private String versionId;

    @Column(name = "last_modified_at")
    private LocalDateTime lastModifiedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modified_by_user_id", foreignKey = @ForeignKey(name = "fk_onedrive_user"))
    private User modifiedBy;

    @Column(name = "modified_by_display_name", length = 100)
    private String modifiedByDisplayName;

    @Lob
    @Column(name = "download_url")
    private String downloadUrl;
}
