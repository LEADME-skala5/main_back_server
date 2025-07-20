package com.sk.skala.skore.auth.user.dto;

import com.sk.skala.skore.auth.user.entity.User;
import com.sk.skala.skore.common.entity.CareerLevel;

public record UserResponse(
        Long id,
        String name,
        String employeeNumber,
        Long departmentId,
        Long divisionId,
        Long organizationId,
        Boolean isManager,
        CareerLevel careerLevel
) {
    public UserResponse(User user) {
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