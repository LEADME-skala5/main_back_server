package com.example.main_server.activityLog.teams.teams.chat.repository;

import com.example.main_server.activityLog.teams.teams.chat.entity.TeamsMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamsMessageRepository extends JpaRepository<TeamsMessage, Long> {
}