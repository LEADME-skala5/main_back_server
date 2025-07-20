package com.sk.skala.skore.user.repository;

import com.sk.skala.skore.user.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
