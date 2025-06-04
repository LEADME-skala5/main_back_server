package com.example.main_server.teams.email.repository;

import com.example.main_server.teams.email.entity.OutlookAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface OutlookAttachmentRepository extends JpaRepository<OutlookAttachment, String> {
    List<OutlookAttachment> findByEmailId(String emailId);
}
