package com.runnershigh.presentation.member;

import com.runnershigh.application.common.exception.ForbiddenException;
import com.runnershigh.application.member.MemberService;
import com.runnershigh.infrastructure.security.LoginMember;
import com.runnershigh.presentation.common.ApiResponse;
import com.runnershigh.presentation.member.dto.MemberCreateRequest;
import com.runnershigh.presentation.member.dto.MemberResponse;
import com.runnershigh.presentation.member.dto.MemberUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{id}")
    public ApiResponse<MemberResponse> get(@PathVariable Long id) {
        return ApiResponse.success(MemberResponse.from(memberService.findById(id)));
    }

    @PostMapping
    public ApiResponse<Long> create(@Valid @RequestBody MemberCreateRequest request) {
        return ApiResponse.success(memberService.create(request.toCommand()));
    }

    @PutMapping("/{id}")
    public ApiResponse<Void> update(
            @PathVariable Long id,
            @AuthenticationPrincipal LoginMember loginMember,
            @Valid @RequestBody MemberUpdateRequest request
    ) {
        if (!loginMember.id().equals(id)) {
            throw new ForbiddenException();
        }
        memberService.update(id, request.toCommand());
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal LoginMember loginMember
    ) {
        if (!loginMember.id().equals(id)) {
            throw new ForbiddenException();
        }
        memberService.delete(id);
        return ApiResponse.success(null);
    }
}
