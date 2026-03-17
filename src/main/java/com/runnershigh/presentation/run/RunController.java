package com.runnershigh.presentation.run;

import com.runnershigh.application.run.RunService;
import com.runnershigh.infrastructure.security.LoginMember;
import com.runnershigh.presentation.common.ApiResponse;
import com.runnershigh.presentation.run.dto.RunCreateRequest;
import com.runnershigh.presentation.run.dto.RunResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/runs")
@RequiredArgsConstructor
public class RunController {

    private final RunService runService;

    @GetMapping("/members/{memberId}")
    public ApiResponse<List<RunResponse>> getByMember(@PathVariable Long memberId) {
        return ApiResponse.success(
                runService.findByMemberId(memberId).stream()
                        .map(RunResponse::from)
                        .toList()
        );
    }

    @PostMapping
    public ApiResponse<Long> create(
            @AuthenticationPrincipal LoginMember loginMember,
            @Valid @RequestBody RunCreateRequest request
    ) {
        return ApiResponse.success(runService.create(loginMember.id(), request.toCommand()));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        runService.delete(id);
        return ApiResponse.success(null);
    }
}
