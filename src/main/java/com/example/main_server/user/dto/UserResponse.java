package com.example.main_server.user.dto;

import com.example.main_server.common.entity.User;

public record UserResponse(
        Long id,
        String name,
        String employeeNumber,
        Boolean isManager
) {
    public UserResponse(User user) {
        this(
                user.getId(),
                user.getName(),
                user.getEmployeeNumber(),
                user.getIsManager()
        );
    }
}