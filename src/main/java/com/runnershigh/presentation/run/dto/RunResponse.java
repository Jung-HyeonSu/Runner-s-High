package com.runnershigh.presentation.run.dto;

import com.runnershigh.domain.run.entity.Run;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RunResponse {
    private Long id;
    private Long memberId;
    private Long courseId;
    private double distance;
    private LocalDateTime createdAt;

    public static RunResponse from(Run run) {
        return new RunResponse(
                run.getId(),
                run.getMemberId(),
                run.getCourseId(),
                run.getDistance(),
                run.getCreatedAt()
        );
    }
}
