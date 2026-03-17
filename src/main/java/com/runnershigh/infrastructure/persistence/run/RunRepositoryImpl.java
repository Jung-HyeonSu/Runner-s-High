package com.runnershigh.infrastructure.persistence.run;

import com.runnershigh.domain.run.entity.Run;
import com.runnershigh.domain.run.repository.RunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RunRepositoryImpl implements RunRepository {

    private final RunJpaRepository jpaRepository;

    @Override
    public Run save(Run run) {
        return jpaRepository.save(run);
    }

    @Override
    public Optional<Run> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Run> findByMemberId(Long memberId) {
        return jpaRepository.findByMemberId(memberId);
    }

    @Override
    public void delete(Run run) {
        jpaRepository.delete(run);
    }
}
