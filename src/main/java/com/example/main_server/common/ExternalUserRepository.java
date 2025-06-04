package com.example.main_server.common;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExternalUserRepository extends JpaRepository<ExternalUser, Long> {
}
