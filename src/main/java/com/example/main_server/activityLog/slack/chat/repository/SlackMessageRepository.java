package com.example.main_server.activityLog.slack.chat.repository;

import com.example.main_server.activityLog.slack.chat.entity.SlackMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlackMessageRepository extends JpaRepository<SlackMessage, Long> {
}
