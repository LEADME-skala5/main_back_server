package com.sk.skala.skore.user.repository;

import com.sk.skala.skore.user.entity.ExternalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalUserRepository extends JpaRepository<ExternalUser, Long> {
}
