package com.delivery_signal.eureka.client.user.controller;

import com.delivery_signal.eureka.client.user.dto.request.UserCreateRequestDto;
import com.delivery_signal.eureka.client.user.dto.response.UserResponseDto;
import com.delivery_signal.eureka.client.user.entity.UserRole;
import com.delivery_signal.eureka.client.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import io.swagger.v3.oas.annotations.tags.Tag;
//import io.swagger.v3.oas.annotations.Operation;

@Slf4j
@RestController
@RequiredArgsConstructor
//@Tag(name="user-controller", description = "MASTER의 생성/수정/삭제 + 모든 사용자의 권한별 조회 API")
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping()
//    @PreAuthorize("hasRole('MASTER')")
//    @Operation(summary="MASTER의 사용자 생성", description="새로운 사용자를 등록합니다")
    public UserResponseDto registerUser(UserCreateRequestDto requestDto) {
        UserResponseDto responseDto = userService.createUser(requestDto);

        return responseDto;
    }


}
