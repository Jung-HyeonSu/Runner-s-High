package com.runnershigh.application.course.dto;

import java.util.List;

public record CourseCreateCommand(
        String name,
        String region,
        double distance,
        List<WaypointCommand> waypoints
) {
    public record WaypointCommand(double latitude, double longitude, int sequence) {
    }
}
