package com.example.main_server.teams.email.repository;

import com.example.main_server.teams.email.entity.OutlookAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutlookAttachmentRepository extends JpaRepository<OutlookAttachment, Long> {
}
