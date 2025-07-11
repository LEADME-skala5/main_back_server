package com.example.main_server.auth.user;

import com.example.main_server.auth.jwt.JwtRedisService;
import com.example.main_server.auth.jwt.JwtTokenProvider;
import com.example.main_server.auth.user.dto.LogInRequest;
import com.example.main_server.auth.user.dto.LogInResponse;
import com.example.main_server.auth.user.dto.UserRegisterRequest;
import com.example.main_server.auth.user.dto.UserResponse;
import com.example.main_server.auth.user.entity.User;
import com.example.main_server.auth.user.exception.InvalidRefreshTokenException;
import com.example.main_server.auth.user.exception.LogoutFailedException;
import com.example.main_server.auth.user.exception.RefreshTokenMismatchException;
import com.example.main_server.auth.user.exception.RefreshTokenNotFoundException;
import com.example.main_server.auth.user.exception.TokenRefreshFailedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtRedisService jwtRedisService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRegisterRequest request) {
        UserResponse response = userService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LogInResponse> login(@RequestBody LogInRequest req,
                                               HttpServletRequest httpReq,
                                               HttpServletResponse httpRes) {
        User user = userService.login(req.employeeNumber(), req.password());

        String access = jwtTokenProvider.createAccessToken(user);
        String refresh = jwtTokenProvider.createRefreshToken(user);

        jwtRedisService.saveRefreshToken(user.getId(), refresh, jwtTokenProvider.getRefreshExpiration());
        jwtTokenProvider.setRefreshTokenCookie(httpReq, httpRes, refresh);
        jwtTokenProvider.setAccessTokenCookie(httpReq, httpRes, access);

        return ResponseEntity.ok(new LogInResponse(user));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<LogInResponse> refresh(HttpServletRequest req, HttpServletResponse res) {

        String refresh = jwtTokenProvider.getRefreshTokenFromCookie(req);
        if (refresh == null) {
            throw new RefreshTokenNotFoundException("리프레시 토큰이 없습니다.");
        }

        if (!jwtTokenProvider.validateToken(refresh)) {
            throw new InvalidRefreshTokenException("유효하지 않은 리프레시 토큰입니다.");
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(refresh);
        if (!jwtRedisService.validateRefreshToken(userId, refresh)) {
            throw new RefreshTokenMismatchException("저장된 리프레시 토큰과 일치하지 않습니다.");
        }

        try {
            User user = userService.getUserById(userId);
            String newAccess = jwtTokenProvider.createAccessToken(user);
            String newRefresh = jwtTokenProvider.createRefreshToken(user);

            jwtRedisService.saveRefreshToken(userId, newRefresh, jwtTokenProvider.getRefreshExpiration());
            jwtTokenProvider.setRefreshTokenCookie(req, res, newRefresh);
            jwtTokenProvider.setAccessTokenCookie(req, res, newAccess);

            return ResponseEntity.ok(new LogInResponse(user));
        } catch (Exception e) {
            throw new TokenRefreshFailedException("토큰 갱신 중 오류가 발생했습니다.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest req, HttpServletResponse res) {
        try {
            String access = jwtTokenProvider.getAccessTokenFromRequest(req);
            if (access != null && jwtTokenProvider.validateToken(access)) {
                Long userId = jwtTokenProvider.getUserIdFromToken(access);
                jwtRedisService.deleteRefreshToken(userId);
            }
            jwtTokenProvider.clearRefreshTokenCookie(req, res);
            jwtTokenProvider.clearAccessTokenCookie(req, res);
            return ResponseEntity.ok(Map.of("message", "로그아웃 되었습니다."));
        } catch (Exception e) {
            throw new LogoutFailedException("로그아웃 중 오류가 발생했습니다.");
        }
    }
}
