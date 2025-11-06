package com.delivery_signal.eureka.client.user.domain.mapper;

import com.delivery_signal.eureka.client.user.presentation.dto.request.UserCreateRequestDto;
import com.delivery_signal.eureka.client.user.presentation.dto.response.UserResponseDto;
import com.delivery_signal.eureka.client.user.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getUserId(), user.getUsername(), user.getSlackId(),
                user.getOrganization(), user.getRole(), user.getApprovalStatus()
        );
    }


    public User toEntity(UserCreateRequestDto requestDto) {
        return User.builder()
                .username(requestDto.username())
                .password(requestDto.password())
                .slackId(requestDto.slackId())
                .organization(requestDto.organization())
                .role(requestDto.role())
                .build();
    }
}
