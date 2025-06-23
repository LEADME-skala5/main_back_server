package com.example.main_server.activityLog.slack.chat.repository;

import com.example.main_server.activityLog.slack.chat.entity.SlackFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlackFileRepository extends JpaRepository<SlackFile, Long> {
}
