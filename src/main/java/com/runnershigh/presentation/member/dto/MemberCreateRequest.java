package com.runnershigh.presentation.member.dto;

import com.runnershigh.application.member.dto.MemberCreateCommand;
import com.runnershigh.domain.member.entity.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberCreateRequest {

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    @NotBlank(message = "이메일은 필수입니다.")
    @jakarta.validation.constraints.Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    public MemberCreateCommand toCommand() {
        return new MemberCreateCommand(
                nickname,
                new Email(email),
                password
        );
    }
}
