package com.runnershigh.application.auth;

import com.runnershigh.application.common.exception.InvalidCredentialsException;
import com.runnershigh.application.common.exception.UnauthorizedException;
import com.runnershigh.application.member.dto.MemberLoginCommand;
import com.runnershigh.domain.member.entity.Email;
import com.runnershigh.domain.member.entity.Member;
import com.runnershigh.domain.member.repository.MemberRepository;
import com.runnershigh.infrastructure.security.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public LoginResult login(MemberLoginCommand command) {
        Email email = new Email(command.email());
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(command.password(), member.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return new LoginResult(issueTokens(member), member);
    }

    public String refresh(String refreshToken) {
        if (!jwtProvider.isValid(refreshToken)) {
            throw new UnauthorizedException();
        }
        Claims claims = jwtProvider.parseClaims(refreshToken);
        return jwtProvider.generateAccessToken(
                Long.parseLong(claims.getSubject()),
                claims.get("email", String.class),
                claims.get("nickname", String.class)
        );
    }

    private TokenPair issueTokens(Member member) {
        String accessToken = jwtProvider.generateAccessToken(
                member.getId(), member.getEmail().getValue(), member.getNickname());
        String refreshToken = jwtProvider.generateRefreshToken(
                member.getId(), member.getEmail().getValue(), member.getNickname());
        return new TokenPair(accessToken, refreshToken);
    }

    public record TokenPair(String accessToken, String refreshToken) {
    }

    public record LoginResult(TokenPair tokens, Member member) {
    }
}
