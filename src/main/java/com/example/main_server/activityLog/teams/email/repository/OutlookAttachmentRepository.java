package com.example.main_server.activityLog.teams.email.repository;

import com.example.main_server.activityLog.teams.email.entity.OutlookAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutlookAttachmentRepository extends JpaRepository<OutlookAttachment, Long> {
}
