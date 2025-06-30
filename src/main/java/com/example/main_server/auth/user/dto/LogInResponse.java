package com.example.main_server.auth.user.dto;

import com.example.main_server.auth.user.entity.User;
import com.example.main_server.common.entity.CareerLevel;

public record LogInResponse(
        Long id,
        String name,
        String employeeNumber,
        Long departmentId,
        Long divisionId,
        Long organizationId,
        Boolean isManager,
        CareerLevel careerLevel
) {
    public LogInResponse(User user) {
        this(
                user.getId(),
                user.getName(),
                user.getEmployeeNumber(),
                user.getDepartment() != null ? user.getDepartment().getId() : null,
                user.getDivision() != null ? user.getDivision().getId() : null,
                user.getOrganization() != null ? user.getOrganization().getId() : null,
                user.getIsManager(),
                user.getCareerLevel()
        );
    }
}
