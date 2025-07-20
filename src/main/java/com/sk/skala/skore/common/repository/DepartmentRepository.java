package com.sk.skala.skore.common.repository;

import com.sk.skala.skore.common.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
