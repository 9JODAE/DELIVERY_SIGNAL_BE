package com.delivery_signal.eureka.client.user.mapper;

import com.delivery_signal.eureka.client.user.dto.request.UserCreateRequestDto;
import com.delivery_signal.eureka.client.user.dto.response.UserResponseDto;
import com.delivery_signal.eureka.client.user.entity.User;
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
                .username(requestDto.getUsername())
                .password(requestDto.getPassword())
                .slackId(requestDto.getSlackId())
                .organization(requestDto.getOrganization())
                .role(requestDto.getRole())
                .created
                .build();
    }
}
