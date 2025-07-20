package com.sk.skala.skore.activityLog.teams.email.repository;

import com.sk.skala.skore.activityLog.teams.email.entity.OutlookRecipient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutlookRecipientRepository extends JpaRepository<OutlookRecipient, Long> {
}
