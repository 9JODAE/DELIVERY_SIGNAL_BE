package com.delivery_signal.eureka.client.user.presentation.mapper;

import com.delivery_signal.eureka.client.user.application.command.CreateUserCommand;
import com.delivery_signal.eureka.client.user.application.dto.ApprovalStatusType;
import com.delivery_signal.eureka.client.user.application.dto.UserResult;
import com.delivery_signal.eureka.client.user.application.dto.UserRoleType;
import com.delivery_signal.eureka.client.user.presentation.dto.request.CreateUserRequest;
import com.delivery_signal.eureka.client.user.presentation.dto.response.GetUserResponse;
import com.delivery_signal.eureka.client.user.domain.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public GetUserResponse from(User user) {
        return new GetUserResponse(
                user.getUserId(), user.getUsername(), user.getSlackId(),
                user.getOrganization(), user.getOrganizationId(), UserRoleType.from(user.getRole()), ApprovalStatusType.from(user.getApprovalStatus())
        );
    }


    public User toEntity(CreateUserRequest requestDto) {
        return User.builder()
                .username(requestDto.username())
                .password(passwordEncoder.encode(requestDto.password()))
                .slackId(requestDto.slackId())
                .organization(requestDto.organization())
                .organizationId(requestDto.organizationId())
                .role(requestDto.role().toDomain())
                .build();
    }

    public UserResult toUserResultDto(CreateUserCommand command) {
        return new UserResult(
                null, command.username(), passwordEncoder.encode(command.password()), command.slackId(),
                command.organization(), command.organizationId(), command.role().toDomain()
        );
    }

    public UserResult toUserResultDto(Long userId, CreateUserCommand command) {
        return new UserResult(
                userId, command.username(), passwordEncoder.encode(command.password()), command.slackId(),
                command.organization(), command.organizationId(), command.role().toDomain()
        );
    }
}
