package com.example.main_server.user;

import com.example.main_server.auth.jwt.JwtRedisService;
import com.example.main_server.auth.jwt.JwtTokenProvider;
import com.example.main_server.common.entity.User;
import com.example.main_server.user.dto.LogInRequest;
import com.example.main_server.user.dto.LogInResponse;
import com.example.main_server.user.dto.UserRegisterRequest;
import com.example.main_server.user.dto.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> login(@RequestBody LogInRequest request, HttpServletResponse response) {
        try {
            // 사용자 인증
            User user = userService.login(request.employeeNumber(), request.password());

            // 액세스 토큰 생성
            String accessToken = jwtTokenProvider.createAccessToken(user);

            // 리프레시 토큰 생성
            String refreshToken = jwtTokenProvider.createRefreshToken(user);

            // 리프레시 토큰을 Redis에 저장
            jwtRedisService.saveRefreshToken(user.getId(), refreshToken, jwtTokenProvider.getRefreshExpiration());

            // 리프레시 토큰을 쿠키에 설정
            jwtTokenProvider.setRefreshTokenCookie(response, refreshToken);

            // 액세스 토큰을 쿠키에 설정
            jwtTokenProvider.setAccessTokenCookie(response, accessToken);

            return ResponseEntity.ok(new LogInResponse(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(
                    Map.of("errorCode", "LOGIN_FAILED", "message", e.getMessage())
            );
        }
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        // 쿠키에서 리프레시 토큰 추출
        String refreshToken = jwtTokenProvider.getRefreshTokenFromCookie(request);

        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("errorCode", "REFRESH_TOKEN_NOT_FOUND", "message", "리프레시 토큰이 없습니다."));
        }

        try {
            // 리프레시 토큰 유효성 검증
            if (!jwtTokenProvider.validateToken(refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("errorCode", "INVALID_REFRESH_TOKEN", "message", "유효하지 않은 리프레시 토큰입니다."));
            }

            // 토큰에서 사용자 ID 추출
            Long userId = jwtTokenProvider.getUserIdFromToken(refreshToken);

            // Redis에 저장된 토큰과 비교
            if (!jwtRedisService.validateRefreshToken(userId, refreshToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("errorCode", "REFRESH_TOKEN_MISMATCH", "message", "저장된 리프레시 토큰과 일치하지 않습니다."));
            }

            // 사용자 정보 조회
            User user = userService.getUserById(userId);

            // 새 토큰 발급
            String newAccessToken = jwtTokenProvider.createAccessToken(user);
            String newRefreshToken = jwtTokenProvider.createRefreshToken(user);

            // Redis 업데이트
            jwtRedisService.saveRefreshToken(userId, newRefreshToken, jwtTokenProvider.getRefreshExpiration());

            // 쿠키 업데이트
            jwtTokenProvider.setRefreshTokenCookie(response, newRefreshToken);

            // 액세스 토큰을 쿠키에 설정
            jwtTokenProvider.setAccessTokenCookie(response, newAccessToken);

            return ResponseEntity.ok(new LogInResponse(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("errorCode", "TOKEN_REFRESH_FAILED", "message", "토큰 갱신 중 오류가 발생했습니다."));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 액세스 토큰에서 사용자 ID 추출
            String accessToken = jwtTokenProvider.getAccessTokenFromRequest(request);
            if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
                Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);

                // Redis에서 리프레시 토큰 삭제
                jwtRedisService.deleteRefreshToken(userId);
            }

            // 쿠키에서 토큰 삭제
            jwtTokenProvider.clearRefreshTokenCookie(response);
            jwtTokenProvider.clearAccessTokenCookie(response);

            return ResponseEntity.ok(Map.of("message", "로그아웃 되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("errorCode", "LOGOUT_FAILED", "message", "로그아웃 중 오류가 발생했습니다."));
        }
    }
}

