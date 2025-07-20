package com.sk.skala.skore.activityLog.teams.auditLog.repository;

import com.sk.skala.skore.activityLog.teams.auditLog.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, String> {
}
