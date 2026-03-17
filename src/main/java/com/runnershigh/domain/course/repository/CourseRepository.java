package com.runnershigh.domain.course.repository;

import com.runnershigh.domain.course.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {
    Course save(Course course);
    Optional<Course> findById(Long id);
    List<Course> findAllOrderByLikesDesc();
    List<Course> findByRegionOrderByLikesDesc(String region);
    List<Course> findByMemberId(Long memberId);
    void delete(Course course);
}
