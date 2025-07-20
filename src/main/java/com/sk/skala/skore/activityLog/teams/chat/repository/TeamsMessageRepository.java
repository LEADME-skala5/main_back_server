package com.sk.skala.skore.activityLog.teams.chat.repository;

import com.sk.skala.skore.activityLog.teams.chat.entity.TeamsMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamsMessageRepository extends JpaRepository<TeamsMessage, Long> {
}