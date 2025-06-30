package com.example.main_server.activityLog.teams.chat.repository;

import com.example.main_server.activityLog.teams.chat.entity.TeamsMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamsMessageRepository extends JpaRepository<TeamsMessage, Long> {
}