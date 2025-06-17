package com.example.main_server.user.dto;

import com.example.main_server.common.entity.CareerLevel;

public record UserRegisterRequest(
        String name,
        String employeeNumber,
        String password,
        String teamsEmail,
        String slackEmail,
        String localPath,
        Long departmentId,
        Long divisionId,
        Long organizationId,
        Boolean isManager,
        CareerLevel careerLevel
) {
}