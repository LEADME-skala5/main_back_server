package com.example.main_server.activityLog.slack.team.repository;

import com.example.main_server.activityLog.slack.team.entity.SlackTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlackTeamRepository extends JpaRepository<SlackTeam, Long> {
}
