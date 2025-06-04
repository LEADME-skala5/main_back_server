package com.example.main_server.common.repository;

import com.example.main_server.common.entity.ExternalUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExternalUserRepository extends JpaRepository<ExternalUser, Long> {
}
