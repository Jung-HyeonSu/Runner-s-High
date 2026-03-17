package com.runnershigh.application.course;

import com.runnershigh.application.common.exception.EntityNotFoundException;
import com.runnershigh.application.common.exception.ErrorCode;
import com.runnershigh.application.course.dto.CourseCreateCommand;
import com.runnershigh.domain.course.entity.Course;
import com.runnershigh.domain.course.entity.CourseWaypoint;
import com.runnershigh.domain.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;

    public Course findById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.COURSE_NOT_FOUND.getMessage()));
    }

    public List<Course> findAll() {
        return courseRepository.findAllOrderByLikesDesc();
    }

    public List<Course> findByRegion(String region) {
        return courseRepository.findByRegionOrderByLikesDesc(region);
    }

    public List<Course> findByMemberId(Long memberId) {
        return courseRepository.findByMemberId(memberId);
    }

    @Transactional
    public Long create(Long memberId, String creatorNickname, CourseCreateCommand command) {
        List<CourseWaypoint> waypoints = command.waypoints().stream()
                .map(w -> CourseWaypoint.of(w.latitude(), w.longitude(), w.sequence()))
                .toList();

        Course course = Course.create(
                command.name(),
                command.region(),
                memberId,
                creatorNickname,
                command.distance(),
                waypoints
        );
        return courseRepository.save(course).getId();
    }

    @Transactional
    public void like(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.COURSE_NOT_FOUND.getMessage()));
        course.like();
    }

    @Transactional
    public void delete(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.COURSE_NOT_FOUND.getMessage()));
        courseRepository.delete(course);
    }
}
