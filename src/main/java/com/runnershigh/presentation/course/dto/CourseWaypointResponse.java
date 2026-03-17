package com.runnershigh.presentation.course.dto;

import com.runnershigh.domain.course.entity.CourseWaypoint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CourseWaypointResponse {
    private double latitude;
    private double longitude;
    private int sequence;

    public static CourseWaypointResponse from(CourseWaypoint waypoint) {
        return new CourseWaypointResponse(
                waypoint.getLatitude(),
                waypoint.getLongitude(),
                waypoint.getSequence()
        );
    }
}
