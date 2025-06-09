package com.example.main_server.user.dto;

import com.example.main_server.common.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {
    private Long id;
    private String name;
    private String employeeNumber;
    private Boolean isManager;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.employeeNumber = user.getEmployeeNumber();
        this.isManager = user.getIsManager();
    }
}
