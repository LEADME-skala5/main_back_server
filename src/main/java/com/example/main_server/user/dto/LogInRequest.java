package com.example.main_server.user.dto;

import lombok.Getter;

@Getter
public class LogInRequest {
    private String employeeNumber;
    private String password;
}
