package com.example.main_server.activityLog.teams.email.repository;

import com.example.main_server.activityLog.teams.email.entity.OutlookRecipient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutlookRecipientRepository extends JpaRepository<OutlookRecipient, Long> {
}
