package com.runnershigh.presentation.course.dto;

import com.runnershigh.application.course.dto.CourseCreateCommand;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CourseWaypointRequest {

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    private Integer sequence;

    public CourseCreateCommand.WaypointCommand toCommand() {
        return new CourseCreateCommand.WaypointCommand(latitude, longitude, sequence);
    }
}
