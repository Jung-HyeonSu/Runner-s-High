package com.runnershigh.infrastructure.persistence.member;

import com.runnershigh.domain.member.entity.Email;
import com.runnershigh.domain.member.entity.Member;
import com.runnershigh.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository jpaRepository;

    @Override
    public Member save(Member member) {
        return jpaRepository.save(member);
    }

    @Override
    public Optional<Member> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Member> findByEmail(Email email) {
        return jpaRepository.findByEmail(email);
    }

    @Override
    public Optional<Member> findByNickname(String nickname) {
        return jpaRepository.findByNickname(nickname);
    }

    @Override
    public void delete(Member member) {
        jpaRepository.delete(member);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return jpaRepository.existsByNickname(nickname);
    }
}
