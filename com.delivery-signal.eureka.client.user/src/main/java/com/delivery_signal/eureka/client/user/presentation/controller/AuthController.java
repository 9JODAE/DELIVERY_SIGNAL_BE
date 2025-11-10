package com.delivery_signal.eureka.client.user.presentation.controller;

import com.delivery_signal.eureka.client.user.application.service.JwtUtil;
import com.delivery_signal.eureka.client.user.application.service.UserService;
import com.delivery_signal.eureka.client.user.presentation.dto.ApiResponse;
import com.delivery_signal.eureka.client.user.presentation.dto.request.UserCreateRequestDto;
import com.delivery_signal.eureka.client.user.presentation.dto.response.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/open-api/v1/auth")

public class AuthController {
    private final UserService userService;


    @PostMapping("")
    @Operation(summary="회원가입", description="새로운 사용자가 회원가입을 진행합니다")
    public ResponseEntity<ApiResponse<UserResponseDto>> signUp(@Valid @RequestBody UserCreateRequestDto requestDto) {
        log.info("회원가입 요청 : username={}", requestDto.username());
        UserResponseDto responseDto = userService.createUser(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(responseDto));

    }

    /*
    @PostMapping("/token")
    @Operation(summary="로그인", description="사용자가 로그인합니다")
    */
//      JwtAuthenticationFilter에서 진행
}
