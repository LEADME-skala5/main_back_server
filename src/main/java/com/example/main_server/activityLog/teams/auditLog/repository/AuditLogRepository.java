package com.example.main_server.activityLog.teams.auditLog.repository;

import com.example.main_server.activityLog.teams.auditLog.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, String> {
}
