package com.runnershigh.application.member.dto;

import com.runnershigh.domain.member.entity.Email;

public record MemberCreateCommand(
        String nickname,
        Email email,
        String password
) {
}
