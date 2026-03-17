package com.runnershigh.infrastructure.persistence.run;

import com.runnershigh.domain.run.entity.Run;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RunJpaRepository extends JpaRepository<Run, Long> {
    List<Run> findByMemberId(Long memberId);
}
