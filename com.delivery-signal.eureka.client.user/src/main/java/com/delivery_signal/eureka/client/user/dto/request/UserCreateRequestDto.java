package com.delivery_signal.eureka.client.user.dto.request;

import com.delivery_signal.eureka.client.user.entity.ApprovalStatus;
import com.delivery_signal.eureka.client.user.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserCreateRequestDto {

    @NotBlank(message = "이름은 필수 정보입니다")
    @Size(min = 3, max = 10, message = "이름은 3~10자로 입력해야 합니다")
    @Pattern(regexp = "^[a-z0-9]{3,10}$",
            message = "이름은 알파벳 소문자(a~z)와 숫자(0~9)로만 구성되어야 합니다")
    private String username;

    @NotBlank(message = "비밀번호는 필수 정보입니다")
    @Size(min = 8, max = 15, message = "비밀번호는 8~15자로 입력해야 합니다")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]).*$",
            message = "비밀번호는 알파벳 대소문자와 숫자, 특수문자를 포함해야 합니다")
    private String password;

    @NotBlank(message = "Slack ID는 필수 정보입니다")
    @Size(min = 8, max = 15, message = "비밀번호는 8~15자로 입력해야 합니다")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]).*$",
            message = "비밀번호는 알파벳 대소문자와 숫자, 특수문자를 포함해야 합니다")
    private String slackId;

    @NotBlank(message = "소속 업체(또는 허브)명은 필수 정보입니다")
    @Size(min = 8, max = 15, message = "비밀번호는 8~15자로 입력해야 합니다")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]).*$",
            message = "비밀번호는 알파벳 대소문자와 숫자, 특수문자를 포함해야 합니다")
    private String organization;

    @NotBlank(message = "사용자의 Role은 필수 정보입니다")
    private UserRole role;

    // Master 회원 가입
    private boolean master;
    private String masterToken = "";
}
