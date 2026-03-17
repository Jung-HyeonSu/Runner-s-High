package com.runnershigh.application.member;

import com.runnershigh.application.common.exception.DuplicateException;
import com.runnershigh.application.common.exception.EntityNotFoundException;
import com.runnershigh.application.common.exception.ErrorCode;
import com.runnershigh.application.member.dto.MemberCreateCommand;
import com.runnershigh.application.member.dto.MemberUpdateCommand;
import com.runnershigh.domain.member.entity.Email;
import com.runnershigh.domain.member.entity.Member;
import com.runnershigh.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));
    }

    @Transactional
    public Long create(MemberCreateCommand command) {
        validateDuplicateEmail(command.email());
        validateDuplicateNickname(command.nickname());

        Member member = Member.create(
                command.nickname(),
                command.email(),
                passwordEncoder.encode(command.password())
        );
        return memberRepository.save(member).getId();
    }

    @Transactional
    public void update(Long id, MemberUpdateCommand command) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));

        if (!member.getNickname().equals(command.nickname())) {
            validateDuplicateNickname(command.nickname());
            member.changeNickname(command.nickname());
        }
    }

    @Transactional
    public void delete(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.MEMBER_NOT_FOUND.getMessage()));
        memberRepository.delete(member);
    }

    private void validateDuplicateEmail(Email email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

    private void validateDuplicateNickname(String nickname) {
        if (memberRepository.existsByNickname(nickname)) {
            throw new DuplicateException(ErrorCode.DUPLICATE_NICKNAME);
        }
    }
}
