package com.sk.skala.skore.auth;

import com.sk.skala.skore.auth.exception.AuthException;
import com.sk.skala.skore.auth.exception.AuthExceptionType;
import com.sk.skala.skore.user.UserService;
import com.sk.skala.skore.user.dto.LogInRequest;
import com.sk.skala.skore.user.dto.LogInResponse;
import com.sk.skala.skore.user.dto.UserRegisterRequest;
import com.sk.skala.skore.user.dto.UserResponse;
import com.sk.skala.skore.user.entity.User;
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
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtRedisService jwtRedisService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody UserRegisterRequest request) {
        UserResponse response = userService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
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

    @PostMapping("/refresh")
    public ResponseEntity<LogInResponse> refresh(HttpServletRequest req, HttpServletResponse res) {

        String refresh = jwtTokenProvider.getRefreshTokenFromCookie(req);
        if (refresh == null) {
            throw new AuthException(AuthExceptionType.REFRESH_TOKEN_NOT_FOUND);
        }

        if (!jwtTokenProvider.validateToken(refresh)) {
            throw new AuthException(AuthExceptionType.INVALID_REFRESH_TOKEN);
        }

        Long userId = jwtTokenProvider.getUserIdFromToken(refresh);
        if (!jwtRedisService.validateRefreshToken(userId, refresh)) {
            throw new AuthException(AuthExceptionType.INVALID_REFRESH_TOKEN);
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
            throw new AuthException(AuthExceptionType.TOKEN_REFRESH_FAILED);
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
            throw new AuthException(AuthExceptionType.LOGOUT_FAILED);
        }
    }
}
