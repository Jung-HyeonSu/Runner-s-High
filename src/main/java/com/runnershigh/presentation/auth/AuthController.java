package com.runnershigh.presentation.auth;

import com.runnershigh.application.auth.AuthService;
import com.runnershigh.application.common.exception.UnauthorizedException;
import com.runnershigh.infrastructure.security.jwt.CookieManager;
import com.runnershigh.presentation.common.ApiResponse;
import com.runnershigh.presentation.member.dto.MemberLoginRequest;
import com.runnershigh.presentation.member.dto.MemberResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieManager cookieManager;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<MemberResponse>> login(@Valid @RequestBody MemberLoginRequest request) {
        AuthService.LoginResult result = authService.login(request.toCommand());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,
                        cookieManager.createAccessTokenCookie(result.tokens().accessToken()).toString())
                .header(HttpHeaders.SET_COOKIE,
                        cookieManager.createRefreshTokenCookie(result.tokens().refreshToken()).toString())
                .body(ApiResponse.success(MemberResponse.from(result.member())));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookieManager.clearAccessTokenCookie().toString())
                .header(HttpHeaders.SET_COOKIE, cookieManager.clearRefreshTokenCookie().toString())
                .body(ApiResponse.success(null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<Void>> refresh(
            @CookieValue(name = CookieManager.REFRESH_TOKEN, required = false) String refreshToken
    ) {
        if (refreshToken == null) {
            throw new UnauthorizedException();
        }
        String newAccessToken = authService.refresh(refreshToken);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE,
                        cookieManager.createAccessTokenCookie(newAccessToken).toString())
                .body(ApiResponse.success(null));
    }
}
