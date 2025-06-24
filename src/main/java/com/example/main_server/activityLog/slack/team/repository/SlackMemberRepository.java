package com.example.main_server.activityLog.slack.team.repository;

import com.example.main_server.activityLog.slack.team.entity.SlackMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlackMemberRepository extends JpaRepository<SlackMember, Long> {
}
