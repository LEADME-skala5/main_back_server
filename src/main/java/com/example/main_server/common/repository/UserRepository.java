package com.example.main_server.common.repository;

import com.example.main_server.auth.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByTeamsUserId(String teamsUserId);

    Optional<User> findByEmployeeNumber(String employeeNumber);

    Optional<User> findByTeamsEmail(String teamsEmail);

    Optional<User> findByPrimaryEmail(String primaryEmail);

    List<User> findByOrganizationId(Long organizationId);

    List<User> findAllByOrganizationId(Long organizationId);

    @Query("SELECT u FROM User u JOIN FETCH u.organization JOIN FETCH u.job WHERE u.organization.id = :orgId")
    List<User> findByOrganizationIdWithDetails(@Param("orgId") Long orgId);

}