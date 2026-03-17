package com.runnershigh.domain.course.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseWaypoint {

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(nullable = false)
    private int sequence;

    public static CourseWaypoint of(double latitude, double longitude, int sequence) {
        CourseWaypoint waypoint = new CourseWaypoint();
        waypoint.latitude = latitude;
        waypoint.longitude = longitude;
        waypoint.sequence = sequence;
        return waypoint;
    }
}
