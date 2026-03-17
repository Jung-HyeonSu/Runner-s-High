package com.runnershigh.presentation.course.dto;

import com.runnershigh.application.course.dto.CourseCreateCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class CourseCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String region;

    @Positive
    private double distance;

    @NotNull
    @Size(min = 2)
    @Valid
    private List<CourseWaypointRequest> waypoints;

    public CourseCreateCommand toCommand() {
        return new CourseCreateCommand(
                name,
                region,
                distance,
                waypoints.stream().map(CourseWaypointRequest::toCommand).toList()
        );
    }
}
