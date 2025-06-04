package com.example.main_server.local.repository;

import com.example.main_server.local.entity.Local;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LocalRepository extends JpaRepository<Local, Long>  {
}
