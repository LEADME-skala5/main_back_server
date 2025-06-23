package com.example.main_server.common.repository;

import com.example.main_server.common.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByTeamsUserId(String teamsUserId);

    Optional<User> findByEmployeeNumber(String employeeNumber);

    Optional<User> findByTeamsEmail(String teamsEmail);

    Optional<User> findByPrimaryEmail(String primaryEmail);

    List<User> findByOrganizationId(Long organizationId);

    List<User> findAllByOrganizationId(Long organizationId);
}