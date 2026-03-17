package com.runnershigh.presentation.course.dto;

import com.runnershigh.domain.course.entity.Course;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CourseResponse {
    private Long id;
    private String name;
    private String region;
    private Long memberId;
    private String creatorNickname;
    private double distance;
    private int likes;
    private List<CourseWaypointResponse> waypoints;
    private LocalDateTime createdAt;

    public static CourseResponse from(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getName(),
                course.getRegion(),
                course.getMemberId(),
                course.getCreatorNickname(),
                course.getDistance(),
                course.getLikes(),
                course.getWaypoints().stream()
                        .map(CourseWaypointResponse::from)
                        .toList(),
                course.getCreatedAt()
        );
    }
}
