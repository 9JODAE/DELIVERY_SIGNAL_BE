package com.delivery_signal.eureka.client.user.service;

import com.delivery_signal.eureka.client.user.dto.request.UserCreateRequestDto;
import com.delivery_signal.eureka.client.user.dto.response.UserResponseDto;
import com.delivery_signal.eureka.client.user.entity.User;
import com.delivery_signal.eureka.client.user.mapper.UserMapper;
import com.delivery_signal.eureka.client.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDto createUser(UserCreateRequestDto requestDto) {
        User user = userMapper.toEntity(requestDto);

        userRepository.save(user);
        return userMapper.from(user);
    }
}
