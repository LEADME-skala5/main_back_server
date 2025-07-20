package com.sk.skala.skore.activityLog.slack.chat.repository;

import com.sk.skala.skore.activityLog.slack.chat.entity.SlackFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlackFileRepository extends JpaRepository<SlackFile, Long> {
}
