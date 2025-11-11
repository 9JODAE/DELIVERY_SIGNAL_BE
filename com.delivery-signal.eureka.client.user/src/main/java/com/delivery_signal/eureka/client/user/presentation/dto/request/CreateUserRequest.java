package com.delivery_signal.eureka.client.user.presentation.dto.request;

import com.delivery_signal.eureka.client.user.domain.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateUserRequest(

        @NotBlank(message = "이름은 필수 정보입니다")
        @Size(min = 3, max = 10, message = "이름은 3~10자로 입력해야 합니다")
        @Pattern(
                regexp = "^[a-z0-9]{3,10}$",
                message = "이름은 알파벳 소문자(a~z)와 숫자(0~9)로만 구성되어야 합니다"
        )
        String username,

        @NotBlank(message = "비밀번호는 필수 정보입니다")
        @Size(min = 8, max = 15, message = "비밀번호는 8~15자로 입력해야 합니다")
        @Pattern(
                regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]).*$",
                message = "비밀번호는 알파벳 대소문자와 숫자, 특수문자를 포함해야 합니다"
        )
        String password,

        @NotBlank(message = "Slack ID는 필수 정보입니다")
        String slackId,

        @NotBlank(message = "소속 그룹은 필수 정보입니다. 허브/업체/배송 관리자가 아니라면, 'delivery-signal'을 입력하세요")
        String organization,
        UUID organizationId,

        @NotNull(message = "사용자의 Role은 필수 정보입니다")
        UserRole role,

        // Master 회원 가입
        boolean isMaster,
        String masterToken

) {}