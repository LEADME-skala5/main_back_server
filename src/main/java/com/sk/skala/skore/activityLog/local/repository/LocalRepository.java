package com.sk.skala.skore.activityLog.local.repository;

import com.sk.skala.skore.activityLog.local.entity.Local;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalRepository extends JpaRepository<Local, Long> {
}
