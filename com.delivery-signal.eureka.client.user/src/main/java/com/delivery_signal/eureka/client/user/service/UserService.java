package com.delivery_signal.eureka.client.user.service;

import com.delivery_signal.eureka.client.user.OrderFeignClient;
import com.delivery_signal.eureka.client.user.common.exception.ErrorCode;
import com.delivery_signal.eureka.client.user.common.exception.ServiceException;
import com.delivery_signal.eureka.client.user.dto.request.UserCreateRequestDto;
import com.delivery_signal.eureka.client.user.dto.response.UserResponseDto;
import com.delivery_signal.eureka.client.user.entity.User;
import com.delivery_signal.eureka.client.user.mapper.UserMapper;
import com.delivery_signal.eureka.client.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final OrderFeignClient orderFeignClient;

    public String getOrderInfo() {
        return orderFeignClient.getOrder();
    }

    public String callOrder() {

        return "User -> Order 호출 성공!" + getOrderInfo();
    }



    public UserResponseDto createUser(UserCreateRequestDto requestDto) {
        User user = userMapper.toEntity(requestDto);

        userRepository.save(user);
        return userMapper.from(user);
    }

    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        return userMapper.from(user);
    }


    public List<UserResponseDto> getUsers(String search) {
        List<String> keywords = validSearchKeywords(search);
        Set<Long> presented = new HashSet<>();
        List<UserResponseDto> responseDtos;

        if (keywords == null) {
            responseDtos = userRepository.findAll().stream().map(userMapper::from).toList();
            return responseDtos;
        } else {
            responseDtos = new ArrayList<>();
        }

        // 검색어 필터링 조회
        for (String k : keywords) {
            userRepository.findAllByUsernameContainingIgnoreCase(k)
                    .forEach(user -> addIfNotPresented(user, presented, responseDtos));

            userRepository.findAllByOrganizationContainingIgnoreCase(k)
                    .forEach(user -> addIfNotPresented(user, presented, responseDtos));

            userRepository.findAll().stream()
                    .filter(u -> u.getRole().name().toLowerCase().contains(k.toLowerCase()))
                    .toList()
                    .forEach(user -> addIfNotPresented(user, presented, responseDtos));
        }

        return responseDtos;
    }



    private List<String> validSearchKeywords(String keyword) {

        if (keyword == null || keyword.isEmpty()) {
            return null;
        }
        return List.of(keyword.split("\\s+"));
    }

    private void addIfNotPresented(User user,Set<Long> presented, List<UserResponseDto> responseDtos) {

        if (presented.add(user.getUserId())) {  // 중복 여부 체크
            responseDtos.add(userMapper.from(user));  // 중복 없을 때만 DTO 변환
        }
    }
}
