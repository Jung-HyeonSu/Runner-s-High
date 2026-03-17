package com.runnershigh.infrastructure.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;

    public String generateAccessToken(Long memberId, String email, String nickname) {
        return buildToken(memberId, email, nickname,
                jwtProperties.accessTokenExpiryMinutes() * 60 * 1000L);
    }

    public String generateRefreshToken(Long memberId, String email, String nickname) {
        return buildToken(memberId, email, nickname,
                jwtProperties.refreshTokenExpiryMinutes() * 60 * 1000L);
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims parseClaimsIgnoreExpiry(String token) {
        try {
            return parseClaims(token);
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private String buildToken(Long memberId, String email, String nickname, long expiryMs) {
        Date now = new Date();
        return Jwts.builder()
                .subject(memberId.toString())
                .claim("email", email)
                .claim("nickname", nickname)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expiryMs))
                .signWith(secretKey())
                .compact();
    }

    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    }
}
