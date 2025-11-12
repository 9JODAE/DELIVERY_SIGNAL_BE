package com.delivery_signal.eureka.client.user.application.service;

import com.delivery_signal.eureka.client.user.application.port.out.OrderQueryPort;
import com.delivery_signal.eureka.client.user.application.mapper.UserMapper;
import com.delivery_signal.eureka.client.user.application.exception.ErrorCode;
import com.delivery_signal.eureka.client.user.application.exception.ServiceException;
import com.delivery_signal.eureka.client.user.application.dto.UserRoleType;
import com.delivery_signal.eureka.client.user.application.dto.response.GetUserAuthorizationResponse;
import com.delivery_signal.eureka.client.user.application.dto.request.CreateUserRequest;
import com.delivery_signal.eureka.client.user.application.dto.request.CheckUserRoleRequest;
import com.delivery_signal.eureka.client.user.application.dto.request.UpdateUserApprovalStatusRequest;
import com.delivery_signal.eureka.client.user.application.dto.request.UpdateUserRequest;
import com.delivery_signal.eureka.client.user.application.dto.response.GetUserResponse;

import com.delivery_signal.eureka.client.user.domain.entity.ApprovalStatus;
import com.delivery_signal.eureka.client.user.domain.entity.User;
import com.delivery_signal.eureka.client.user.domain.repository.UserRepository;
import com.delivery_signal.eureka.client.user.domain.entity.UserRole;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final OrderQueryPort orderQueryPort;

    // MASTER_TOKEN
    @Value("${master.token}")
    private String MASTER_TOKEN;


    // Feign Client
//    private final OrderFeignClient orderFeignClient;

    // 통신 테스트 (Other Service -> User Service)

    // 통신 테스트 (User Service -> Other Service)

//    public String getOrderInfo() {
//        return orderFeignClient.getOrder();
//    }

//    @Transactional
//    public String callOrder() {
//        return "User -> Order 호출 성공!" + getOrderInfo();
//    }

    @Transactional
    public String callOrder() {
        return "User -> Order 호출 성공!" + orderQueryPort.test();
    }

    // User 권한 검증
    @Transactional
    public GetUserAuthorizationResponse checkUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        if (user.isDeleted()) {
            return null;
        }
        UserRoleType userRole = UserRoleType.from(user.getRole());
        String organization = user.getOrganization();
        UUID organizationId = user.getOrganizationId();

        return new GetUserAuthorizationResponse(userId, userRole, organization, organizationId);
    }


    @Transactional
    public Boolean checkUserRole(CheckUserRoleRequest requestDto) {
        User user = userRepository.findById(requestDto.userId()).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));
        UserRoleType userRole = UserRoleType.from(user.getRole());
        UserRoleType userRoleType = requestDto.role();

        return userRole.equals(userRoleType);
    }


    // User 생성
    @Transactional
    public GetUserResponse createUser(CreateUserRequest requestDto) {
        User user = userMapper.toEntity(requestDto);
        // 사용자 중복 확인
        Optional<User> duplicated = userRepository.findBySlackId(requestDto.slackId());
        if (!duplicated.isEmpty()) {
            System.out.print(duplicated.get().getUsername());
            System.out.print(duplicated.get().getSlackId());
            throw new IllegalArgumentException(ErrorCode.USER_USERNAME_DUPLICATED.getMessage());
        }

        // Master인 경우, 승인 상태 PENDING -> APPROVED 자동 조정
        if (user.getRole().equals(UserRole.MASTER)) {
            user.updateApprovalStatus(ApprovalStatus.APPROVED);
        }

        // organization id가 'delivery-signal'인 경우, organizationId=null
        if (user.getOrganization() == "delivery-signal") {
            user.updateOrganizationId(null);
        }
        userRepository.save(user);
        return userMapper.from(user);
    }

    // 특정 User 조회
    @Transactional
    public GetUserResponse getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

        if (user.isDeleted()) {
            return null;
        }

        return userMapper.from(user);
    }


    // User 목록 조회 및 검색
    @Transactional
    public List<GetUserResponse> getUsers(String search) {
        List<String> keywords = validSearchKeywords(search);
        Set<Long> presented = new HashSet<>();
        List<GetUserResponse> responseDtos;

        if (keywords == null) {
            responseDtos = userRepository.findAll().stream().filter(user -> !user.isDeleted()).map(userMapper::from).toList();
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
    public GetUserResponse updateApprovalStatus(Long userId, UpdateUserApprovalStatusRequest requestDto) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException(ErrorCode.USER_NOT_FOUND));

        if (user.isDeleted()) {
            return null;
        }

        ApprovalStatus status = requestDto.approvalStatus();
        user.updateApprovalStatus(status);

        return userMapper.from(user);
    }


    @Transactional
    public GetUserResponse updateUser(Long userId, UpdateUserRequest requestDto) {
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

    
    
    private void addIfNotPresented(User user,Set<Long> presented, List<GetUserResponse> responseDtos) {

        if (!user.isDeleted()) {
            if (presented.add(user.getUserId())) {
                responseDtos.add(userMapper.from(user));
            }
        }
    }

}
