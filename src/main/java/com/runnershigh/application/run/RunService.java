package com.runnershigh.application.run;

import com.runnershigh.application.common.exception.EntityNotFoundException;
import com.runnershigh.application.common.exception.ErrorCode;
import com.runnershigh.application.run.dto.RunCreateCommand;
import com.runnershigh.domain.member.entity.Member;
import com.runnershigh.domain.member.repository.MemberRepository;
import com.runnershigh.domain.run.entity.Run;
import com.runnershigh.domain.run.repository.RunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RunService {

    private final RunRepository runRepository;
    private final MemberRepository memberRepository;

    public List<Run> findByMemberId(Long memberId) {
        return runRepository.findByMemberId(memberId);
    }

    @Transactional
    public Long create(Long memberId, RunCreateCommand command) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        Run run = Run.create(memberId, command.courseId(), command.distance());
        Run saved = runRepository.save(run);

        // 멤버의 총 러닝 거리 누적
        member.addDistance(command.distance());

        return saved.getId();
    }

    @Transactional
    public void delete(Long id) {
        Run run = runRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.RUN_NOT_FOUND.getMessage()));
        runRepository.delete(run);
    }
}
