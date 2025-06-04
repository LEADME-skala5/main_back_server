package com.example.main_server.slack.chat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SlackFileRepository extends JpaRepository<SlackFile, Long> {
}
