package com.example.main_server.slack.team;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SlackTeamRepository extends JpaRepository<SlackTeam, Long> {
}
