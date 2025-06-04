package com.example.main_server.teams.email.repository;

import com.example.main_server.teams.email.entity.OutlookEmail;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OutlookEmailRepository extends JpaRepository<OutlookEmail,Long>{
    OutlookEmail findByEmailId(String emailId);
}
