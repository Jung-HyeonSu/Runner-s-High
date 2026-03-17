package com.runnershigh.presentation.member.dto;

import com.runnershigh.application.member.dto.MemberUpdateCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberUpdateRequest {

    @NotBlank(message = "닉네임은 필수입니다.")
    private String nickname;

    public MemberUpdateCommand toCommand() {
        return new MemberUpdateCommand(nickname);
    }
}
