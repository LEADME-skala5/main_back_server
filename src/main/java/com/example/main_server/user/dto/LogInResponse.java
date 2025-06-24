package com.example.main_server.user.dto;

import com.example.main_server.common.entity.CareerLevel;
import com.example.main_server.common.entity.User;

public record LogInResponse(
        Long id,
        String name,
        String employeeNumber,
        Long departmentId,
        Long divisionId,
        Long organizationId,
        Boolean isManager,
        CareerLevel careerLevel,
        String accessToken
) {
    public LogInResponse(User user, String accessToken) {
        this(
                user.getId(),
                user.getName(),
                user.getEmployeeNumber(),
                user.getDepartment() != null ? user.getDepartment().getId() : null,
                user.getDivision() != null ? user.getDivision().getId() : null,
                user.getOrganization() != null ? user.getOrganization().getId() : null,
                user.getIsManager(),
                user.getCareerLevel(),
                accessToken
        );
    }
}
