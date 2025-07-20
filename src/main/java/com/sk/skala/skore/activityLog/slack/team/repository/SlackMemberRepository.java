package com.sk.skala.skore.activityLog.slack.team.repository;

import com.sk.skala.skore.activityLog.slack.team.entity.SlackMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlackMemberRepository extends JpaRepository<SlackMember, Long> {
}
