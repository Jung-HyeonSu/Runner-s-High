package com.runnershigh.domain.member.entity;

import com.runnershigh.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String nickname;

    @Embedded
    private Email email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, columnDefinition = "DOUBLE DEFAULT 0.0")
    private double totalDistance = 0.0;

    public static Member create(String nickname, Email email, String password) {
        Member member = new Member();
        member.nickname = nickname;
        member.email = email;
        member.password = password;
        member.totalDistance = 0.0;
        return member;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void addDistance(double km) {
        this.totalDistance += km;
    }
}
