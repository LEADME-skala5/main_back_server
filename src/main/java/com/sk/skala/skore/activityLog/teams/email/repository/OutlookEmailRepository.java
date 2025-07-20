package com.sk.skala.skore.activityLog.teams.email.repository;

import com.sk.skala.skore.activityLog.teams.email.entity.OutlookEmail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutlookEmailRepository extends JpaRepository<OutlookEmail, Long> {
}
