package com.delivery_signal.eureka.client.user.presentation.dto.request;

import com.delivery_signal.eureka.client.user.domain.model.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequestDto(

        @Size(min = 3, max = 10, message = "이름은 3~10자로 입력해야 합니다")
        @Pattern(
                regexp = "^[a-z0-9]{3,10}$",
                message = "이름은 알파벳 소문자(a~z)와 숫자(0~9)로만 구성되어야 합니다"
        )
        String username,

        @Size(min = 8, max = 15, message = "비밀번호는 8~15자로 입력해야 합니다")
        @Pattern(
                regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=\\-{}\\[\\]:;\"'<>,.?/]).*$",
                message = "비밀번호는 알파벳 대소문자와 숫자, 특수문자를 포함해야 합니다"
        )
        String password,

        // 다른 서비스에 변경 알림 여부 논의 필요
        String slackId,
        String organization,

        UserRole role
) {}