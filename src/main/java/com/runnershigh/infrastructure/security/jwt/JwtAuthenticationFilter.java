package com.runnershigh.infrastructure.security.jwt;

import com.runnershigh.infrastructure.security.LoginMember;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        extractCookieValue(request, CookieManager.ACCESS_TOKEN)
                .filter(jwtProvider::isValid)
                .ifPresent(token -> {
                    Claims claims = jwtProvider.parseClaims(token);
                    LoginMember loginMember = new LoginMember(
                            Long.parseLong(claims.getSubject()),
                            claims.get("email", String.class),
                            claims.get("nickname", String.class)
                    );
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(loginMember, null, List.of());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                });

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractCookieValue(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }
        return Arrays.stream(request.getCookies())
                .filter(c -> cookieName.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
