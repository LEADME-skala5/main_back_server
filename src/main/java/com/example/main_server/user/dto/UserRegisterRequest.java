package com.example.main_server.user.dto;

public record UserRegisterRequest(
        String name,
        String teamsEmail,
        String password,
        String employeeNumber,
        String primaryEmail,
        String slackEmail,
        String careerLevel,
        Boolean isManager,
        Long organizationId
) {
}