package com.runnershigh.domain.course.entity;

import com.runnershigh.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String region;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false, length = 50)
    private String creatorNickname;

    @Column(nullable = false)
    private double distance;

    @Column(nullable = false)
    private int likes = 0;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "course_waypoints", joinColumns = @JoinColumn(name = "course_id"))
    @OrderBy("sequence ASC")
    private List<CourseWaypoint> waypoints = new ArrayList<>();

    public static Course create(String name, String region, Long memberId, String creatorNickname,
                                double distance, List<CourseWaypoint> waypoints) {
        Course course = new Course();
        course.name = name;
        course.region = region;
        course.memberId = memberId;
        course.creatorNickname = creatorNickname;
        course.distance = distance;
        course.waypoints = new ArrayList<>(waypoints);
        return course;
    }

    public void like() {
        this.likes++;
    }
}
