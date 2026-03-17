package com.runnershigh.domain.member.repository;

import com.runnershigh.domain.member.entity.Email;
import com.runnershigh.domain.member.entity.Member;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(Email email);

    Optional<Member> findByNickname(String nickname);

    void delete(Member member);

    boolean existsByEmail(Email email);

    boolean existsByNickname(String nickname);
}
