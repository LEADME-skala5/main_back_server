package com.example.main_server.user;

import com.example.main_server.common.entity.User;
import com.example.main_server.user.dto.LogInRequest;
import com.example.main_server.user.dto.LogInResponse;
import com.example.main_server.user.dto.UserRegisterRequest;
import com.example.main_server.user.dto.UserResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterRequest request) {
        try {
            UserResponse response = userService.register(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "errorCode", "DUPLICATE_EMPLOYEE_NUMBER",
                            "message", e.getMessage()
                    )
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LogInRequest request, HttpSession session) {
        try {
            User user = userService.login(request.employeeNumber(), request.password());
            session.setAttribute("LOGIN_USER", user);
            return ResponseEntity.ok(new LogInResponse(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(
                    Map.of("errorCode", "LOGIN_FAILED", "message", e.getMessage())
            );
        }
    }

    @GetMapping("/users/organization/{organizationId}")
    public ResponseEntity<?> getTeamMembers(@PathVariable Long organizationId) {
        List<User> members = userService.getOrganizationMember(organizationId);

        return ResponseEntity.ok("");
    }
}

