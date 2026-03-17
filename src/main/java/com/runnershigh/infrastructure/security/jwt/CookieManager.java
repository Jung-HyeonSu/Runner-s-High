package com.runnershigh.infrastructure.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class CookieManager {

    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";

    private final JwtProperties jwtProperties;

    public ResponseCookie createAccessTokenCookie(String token) {
        return buildCookie(ACCESS_TOKEN, token,
                Duration.ofMinutes(jwtProperties.accessTokenExpiryMinutes()));
    }

    public ResponseCookie createRefreshTokenCookie(String token) {
        return buildCookie(REFRESH_TOKEN, token,
                Duration.ofMinutes(jwtProperties.refreshTokenExpiryMinutes()));
    }

    public ResponseCookie clearAccessTokenCookie() {
        return buildCookie(ACCESS_TOKEN, "", Duration.ZERO);
    }

    public ResponseCookie clearRefreshTokenCookie() {
        return buildCookie(REFRESH_TOKEN, "", Duration.ZERO);
    }

    private ResponseCookie buildCookie(String name, String value, Duration maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(jwtProperties.cookieSecure())
                .sameSite("Lax")
                .path("/")
                .maxAge(maxAge)
                .build();
    }
}
