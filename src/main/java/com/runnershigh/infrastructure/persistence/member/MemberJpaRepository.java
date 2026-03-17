package com.runnershigh.infrastructure.persistence.member;

import com.runnershigh.domain.member.entity.Email;
import com.runnershigh.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(Email email);

    Optional<Member> findByNickname(String nickname);

    boolean existsByEmail(Email email);

    boolean existsByNickname(String nickname);
}
