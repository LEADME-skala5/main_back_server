package com.example.main_server.auth.jwt;

import com.example.main_server.common.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access-expiration}")
    private long accessExpiration;
    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    // 시크릿 키 생성
    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 액세스 토큰 생성
    public String createAccessToken(User user) {
        return createToken(user, accessExpiration);
    }

    // 리프레시 토큰 생성
    public String createRefreshToken(User user) {
        return createToken(user, refreshExpiration);
    }

    // 토큰 생성 메서드
    private String createToken(User user, long expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("employeeNumber", user.getEmployeeNumber());
        claims.put("name", user.getName());
        if (user.getOrganization() != null) {
            claims.put("organizationId", user.getOrganization().getId());
        }
        if (user.getDivision() != null) {
            claims.put("divisionId", user.getDivision().getId());
        }
        if (user.getDepartment() != null) {
            claims.put("departmentId", user.getDepartment().getId());
        }

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmployeeNumber())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 쿠키에 리프레시 토큰 설정
    public void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(REFRESH_TOKEN, refreshToken);
        cookie.setHttpOnly(true);  // JavaScript에서 접근 불가
        cookie.setSecure(false);   // TODO: 나중에 true로 고쳐야함
        cookie.setPath("/");      // 모든 경로에서 접근 가능
        cookie.setMaxAge((int) (refreshExpiration / 1000));  // 초 단위로 변환
        response.addCookie(cookie);
    }

    // 쿠키에 액세스 토큰 설정
    public void setAccessTokenCookie(HttpServletResponse response, String accessToken) {
        Cookie cookie = new Cookie(ACCESS_TOKEN, accessToken);
        cookie.setHttpOnly(false);  // TODO: 나중에 true로 고쳐야함
        cookie.setSecure(false);   // TODO: 나중에 true로 고쳐야함
        cookie.setPath("/");      // 모든 경로에서 접근 가능
        cookie.setMaxAge((int) (accessExpiration / 1000));  // 초 단위로 변환
        response.addCookie(cookie);
    }

    // 쿠키에서 리프레시 토큰 추출
    public String getRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (REFRESH_TOKEN.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // 쿠키에서 액세스 토큰 추출
    public String getAccessTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (ACCESS_TOKEN.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // 요청에서 액세스 토큰 추출 (쿠키 우선, 없으면 헤더 확인)
    public String getAccessTokenFromRequest(HttpServletRequest request) {
        // 먼저 쿠키에서 확인
        String tokenFromCookie = getAccessTokenFromCookie(request);
        if (tokenFromCookie != null) {
            return tokenFromCookie;
        }

        // 쿠키에 없으면 헤더에서 확인
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰에서 클레임 추출
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // 토큰에서 모든 클레임 추출
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰에서 사용자 ID 추출
    public Long getUserIdFromToken(String token) {
        return getClaimFromToken(token, claims -> Long.parseLong(claims.get("id").toString()));
    }

    // 토큰에서 사용자 사번 추출
    public String getEmployeeNumberFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("employeeNumber").toString());
    }

    // 토큰 만료일 추출
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    // 토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {
        try {
            final Date expiration = getExpirationDateFromToken(token);
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("잘못된 JWT 토큰입니다.");
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    // 리프레시 토큰 만료 시간 반환
    public long getRefreshExpiration() {
        return refreshExpiration;
    }

    // 로그아웃 시 쿠키 삭제
    public void clearRefreshTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(REFRESH_TOKEN, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);  // 즉시 삭제
        response.addCookie(cookie);
    }

    // 액세스 토큰 쿠키 삭제
    public void clearAccessTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(ACCESS_TOKEN, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);  // 즉시 삭제
        response.addCookie(cookie);
    }
}
