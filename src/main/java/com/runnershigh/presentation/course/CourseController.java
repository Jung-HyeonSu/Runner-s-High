package com.runnershigh.presentation.course;

import com.runnershigh.application.course.CourseService;
import com.runnershigh.infrastructure.security.LoginMember;
import com.runnershigh.presentation.common.ApiResponse;
import com.runnershigh.presentation.course.dto.CourseCreateRequest;
import com.runnershigh.presentation.course.dto.CourseResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ApiResponse<List<CourseResponse>> getAll(
            @RequestParam(required = false) String region
    ) {
        if (region != null && !region.isBlank()) {
            return ApiResponse.success(
                    courseService.findByRegion(region).stream()
                            .map(CourseResponse::from)
                            .toList()
            );
        }
        return ApiResponse.success(
                courseService.findAll().stream()
                        .map(CourseResponse::from)
                        .toList()
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<CourseResponse> get(@PathVariable Long id) {
        return ApiResponse.success(CourseResponse.from(courseService.findById(id)));
    }

    @GetMapping("/members/{memberId}")
    public ApiResponse<List<CourseResponse>> getByMember(@PathVariable Long memberId) {
        return ApiResponse.success(
                courseService.findByMemberId(memberId).stream()
                        .map(CourseResponse::from)
                        .toList()
        );
    }

    @PostMapping
    public ApiResponse<Long> create(
            @AuthenticationPrincipal LoginMember loginMember,
            @Valid @RequestBody CourseCreateRequest request
    ) {
        return ApiResponse.success(
                courseService.create(loginMember.id(), loginMember.nickname(), request.toCommand())
        );
    }

    @PostMapping("/{id}/likes")
    public ApiResponse<Void> like(@PathVariable Long id) {
        courseService.like(id);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        courseService.delete(id);
        return ApiResponse.success(null);
    }
}
