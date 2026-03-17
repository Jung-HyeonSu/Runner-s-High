package com.runnershigh.infrastructure.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secret,
        long accessTokenExpiryMinutes,
        long refreshTokenExpiryMinutes,
        boolean cookieSecure
) {
}
