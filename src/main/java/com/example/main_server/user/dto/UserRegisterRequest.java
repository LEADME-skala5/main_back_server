package com.example.main_server.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterRequest {
    private String name;
    private String teamsEmail;
    private String password;
    private String employeeNumber;
    private String primaryEmail;
    private String slackEmail;
    private String careerLevel;
    private Boolean isManager;

    private Long departmentId;
    private Long divisionId;
    private Long organizationId;
}
