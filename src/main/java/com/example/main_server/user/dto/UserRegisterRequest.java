package com.example.main_server.user.dto;

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
        String careerLevel
) {
}