package com.runnershigh.infrastructure.persistence.course;

import com.runnershigh.domain.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseJpaRepository extends JpaRepository<Course, Long> {
    List<Course> findAllByOrderByLikesDesc();
    List<Course> findByRegionOrderByLikesDesc(String region);
    List<Course> findByMemberId(Long memberId);
}
