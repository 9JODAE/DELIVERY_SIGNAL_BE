package com.delivery_signal.eureka.client.user.application.service;

import com.delivery_signal.eureka.client.user.application.mapper.UserMapper;

import com.delivery_signal.eureka.client.user.domain.model.ApprovalStatus;
import com.delivery_signal.eureka.client.user.domain.common.exception.ErrorCode;
import com.delivery_signal.eureka.client.user.domain.common.exception.ServiceException;
import com.delivery_signal.eureka.client.user.domain.model.User;

import com.delivery_signal.eureka.client.user.presentation.controller.OrderFeignClient;
import com.delivery_signal.eureka.client.user.presentation.dto.request.UserCreateRequestDto;
import com.delivery_signal.eureka.client.user.presentation.dto.request.UserUpdateApprovalStatusRequestDto;
import com.delivery_signal.eureka.client.user.presentation.dto.request.UserUpdateRequestDto;
import com.delivery_signal.eureka.client.user.presentation.dto.response.UserResponseDto;

import com.delivery_signal.eureka.client.user.domain.repository.UserRepository;

import jakarta.transaction.Transactional;
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

    // Feign Client
    private final OrderFeignClient orderFeignClient;

    public String getOrderInfo() {
        return orderFeignClient.getOrder();
    }

    @Transactional
    public String callOrder() {
        return "User -> Order 호출 성공!" + getOrderInfo();
    }


    // User 생성
    @Transactional
    public UserResponseDto createUser(UserCreateRequestDto requestDto) {
        User user = userMapper.toEntity(requestDto);

        userRepository.save(user);
        return userMapper.from(user);
    }

    // 특정 User 조회
    @Transactional
    public UserResponseDto getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

        if (user.isDeleted()) {
            return null;
        }

        return userMapper.from(user);
    }


    // User 목록 조회 및 검색
    @Transactional
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

    // 회원가입 승인 상태 조정
    @Transactional
    public UserResponseDto updateApprovalStatus(Long userId, UserUpdateApprovalStatusRequestDto requestDto) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

        if (user.isDeleted()) {
            return null;
        }

        ApprovalStatus status = requestDto.approvalStatus();
        user.updateApprovalStatus(status);

        return userMapper.from(user);
    }


    @Transactional
    public UserResponseDto updateUser(Long userId, UserUpdateRequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

        if (user.isDeleted()) {
            return null;
        }

        if (requestDto.username() != null && !requestDto.username().isBlank()) {
            user.updateUsername(requestDto.username());
        }

        if (requestDto.password() != null && !requestDto.password().isBlank()) {
            user.updatePassword(requestDto.password());
        }

        if (requestDto.slackId() != null && !requestDto.slackId().isBlank()) {
            user.updateSlackId(requestDto.slackId());
        }

        if (requestDto.organization() != null && !requestDto.organization().isBlank()) {
            user.updateOrganization(requestDto.organization());
        }

//        if (requestDto.role() != null) { user.updateRole(requestDto.role()); }

        return userMapper.from(user);
    }

    @Transactional
    public Boolean softDeleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

        if (!user.isDeleted()) {
//            userRepository.deleteById(userId); 대체
            user.softDelete(userId);
            return true;
        }

        return false;
    }




    private List<String> validSearchKeywords(String keyword) {

        if (keyword == null || keyword.isEmpty()) {
            return null;
        }
        return List.of(keyword.split("\\s+"));
    }

    
    
    private void addIfNotPresented(User user,Set<Long> presented, List<UserResponseDto> responseDtos) {

        if (!user.isDeleted()) {
            if (presented.add(user.getUserId())) {
                responseDtos.add(userMapper.from(user));
            }
        }

    }

}
