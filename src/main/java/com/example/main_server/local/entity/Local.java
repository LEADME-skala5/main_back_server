package com.example.main_server.local.entity;

import com.example.main_server.common.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDate;


@Entity
@Table(name = "local_files")
@Getter
@Setter
public class Local {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long fileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "local_user_id", length = 255)
    private String localUserId;

    @Column(name = "local_file_id", length = 255)
    private String localFileId;

    @Lob
    @Column(name = "file_path")
    private String filePath;

    @Column(name = "change_id", length = 255)
    private String changeId;

    @Column(name = "change_time", length = 255)
    private String changeTime;

    @Column(name = "change_type", length = 50)
    private String changeType;

    @Lob
    private String removed;

    @Lob
    private String added;

    @Column(name = "log_date")
    private LocalDate logDate;

    @Column(name = "change_datetime")
    private Timestamp changeDatetime;

    @Column(name = "collected_at")
    private Timestamp collectedAt;
}
