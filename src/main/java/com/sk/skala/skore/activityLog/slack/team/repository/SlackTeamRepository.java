package com.sk.skala.skore.activityLog.slack.team.repository;

import com.sk.skala.skore.activityLog.slack.team.entity.SlackTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlackTeamRepository extends JpaRepository<SlackTeam, Long> {
}
