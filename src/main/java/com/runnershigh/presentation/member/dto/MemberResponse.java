package com.runnershigh.presentation.member.dto;

import com.runnershigh.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MemberResponse {

    private Long id;
    private String nickname;
    private String email;
    private double totalDistance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail().getValue())
                .totalDistance(member.getTotalDistance())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }
}
