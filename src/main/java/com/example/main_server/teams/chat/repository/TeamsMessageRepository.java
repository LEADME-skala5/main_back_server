package com.example.main_server.teams.chat.repository;

import com.example.main_server.teams.chat.entity.TeamsMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamsMessageRepository extends JpaRepository<TeamsMessage, Long> {
}