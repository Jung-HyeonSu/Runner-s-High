package com.runnershigh.presentation.member.dto;

import com.runnershigh.application.member.dto.MemberLoginCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberLoginRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    public MemberLoginCommand toCommand() {
        return new MemberLoginCommand(email, password);
    }
}
