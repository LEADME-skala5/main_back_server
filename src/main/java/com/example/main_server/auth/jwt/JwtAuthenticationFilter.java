package com.example.main_server.auth.jwt;

import com.example.main_server.common.entity.User;
import com.example.main_server.common.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 인증이 필요없는 경로는 처리하지 않음
        String path = request.getRequestURI();
        if (path.startsWith("/api/v1/login") || path.startsWith("/api/v1/register") ||
                path.startsWith("/api/v1/auth/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 요청에서 액세스 토큰 추출
            String accessToken = jwtTokenProvider.getAccessTokenFromHeader(request);

            // 토큰 유효성 검사
            if (accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
                // 토큰에서 사용자 정보 추출
                Long userId = jwtTokenProvider.getUserIdFromToken(accessToken);
                User user = userRepository.findById(userId).orElse(null);

                if (user != null) {
                    // 인증 객체 생성 및 SecurityContext에 설정
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, null);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception e) {
            log.error("JWT 인증 처리 중 오류 발생", e);
        }

        filterChain.doFilter(request, response);
    }
}
