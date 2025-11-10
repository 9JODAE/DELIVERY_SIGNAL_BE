package com.delivery_signal.eureka.client.user.application.mapper;

import com.delivery_signal.eureka.client.user.presentation.dto.request.UserCreateRequestDto;
import com.delivery_signal.eureka.client.user.presentation.dto.response.UserResponseDto;
import com.delivery_signal.eureka.client.user.domain.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getUserId(), user.getUsername(), user.getSlackId(),
                user.getOrganization(), user.getOrganizationId(), user.getRole(), user.getApprovalStatus()
        );
    }


    public User toEntity(UserCreateRequestDto requestDto) {
        return User.builder()
                .username(requestDto.username())
                .password(passwordEncoder.encode(requestDto.password()))
                .slackId(requestDto.slackId())
                .organization(requestDto.organization())
                .organizationId(requestDto.organizationId())
                .role(requestDto.role())
                .build();
    }
}
