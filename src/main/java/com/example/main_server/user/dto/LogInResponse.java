package com.example.main_server.user.dto;

import com.example.main_server.common.entity.User;

public record LogInResponse(Long id,
                            String name,
                            String employeeNumber) {
    public LogInResponse(User user) {
        this(user.getId(), user.getName(), user.getEmployeeNumber());
    }

}
