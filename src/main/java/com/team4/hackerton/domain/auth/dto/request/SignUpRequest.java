package com.team4.hackerton.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignUpRequest {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{8,20}$",
            message = "비밀번호는 영문, 숫자를 포함하여 8자 이상 20자 이하로 입력해주세요."
    )
    private String password;

    @NotBlank(message = "이름은 필수입니다.")
    private String name;
}
