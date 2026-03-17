package com.runnershigh.infrastructure.persistence.course;

import com.runnershigh.domain.course.entity.Course;
import com.runnershigh.domain.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CourseRepositoryImpl implements CourseRepository {

    private final CourseJpaRepository jpaRepository;

    @Override
    public Course save(Course course) {
        return jpaRepository.save(course);
    }

    @Override
    public Optional<Course> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Course> findAllOrderByLikesDesc() {
        return jpaRepository.findAllByOrderByLikesDesc();
    }

    @Override
    public List<Course> findByRegionOrderByLikesDesc(String region) {
        return jpaRepository.findByRegionOrderByLikesDesc(region);
    }

    @Override
    public List<Course> findByMemberId(Long memberId) {
        return jpaRepository.findByMemberId(memberId);
    }

    @Override
    public void delete(Course course) {
        jpaRepository.delete(course);
    }
}
