package com.example.main_server.teams.email.repository;

import com.example.main_server.teams.email.entity.OutlookRecipient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutlookRecipientRepository extends JpaRepository<OutlookRecipient, Long> {
}
