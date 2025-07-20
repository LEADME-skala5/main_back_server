package com.sk.skala.skore.activityLog.slack.chat.repository;

import com.sk.skala.skore.activityLog.slack.chat.entity.SlackMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlackMessageRepository extends JpaRepository<SlackMessage, Long> {
}
