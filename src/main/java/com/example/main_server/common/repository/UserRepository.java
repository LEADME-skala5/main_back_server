package com.example.main_server.common.repository;

import com.example.main_server.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByTeamsUserId(String teamsUserId);

    Optional<User> findByEmployeeNumber(String employeeNumber);

}