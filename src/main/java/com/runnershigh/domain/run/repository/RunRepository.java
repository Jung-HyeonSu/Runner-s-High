package com.runnershigh.domain.run.repository;

import com.runnershigh.domain.run.entity.Run;

import java.util.List;
import java.util.Optional;

public interface RunRepository {
    Run save(Run run);
    Optional<Run> findById(Long id);
    List<Run> findByMemberId(Long memberId);
    void delete(Run run);
}
