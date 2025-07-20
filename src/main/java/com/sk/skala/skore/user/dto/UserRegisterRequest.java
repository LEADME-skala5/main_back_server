package com.sk.skala.skore.user.dto;

import com.sk.skala.skore.user.entity.CareerLevel;

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