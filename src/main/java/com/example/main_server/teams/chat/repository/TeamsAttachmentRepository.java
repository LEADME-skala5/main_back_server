package com.example.main_server.teams.chat.repository;

import com.example.main_server.teams.chat.entity.TeamsAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamsAttachmentRepository extends JpaRepository<TeamsAttachment, Long> {
}