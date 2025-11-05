package com.delivery_signal.eureka.client.user.controller;

import com.delivery_signal.eureka.client.user.dto.request.UserCreateRequestDto;
import com.delivery_signal.eureka.client.user.dto.response.UserResponseDto;
import com.delivery_signal.eureka.client.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//import io.swagger.v3.oas.annotations.tags.Tag;
//import io.swagger.v3.oas.annotations.Operation;

@Slf4j
@RestController
@RequiredArgsConstructor
//@Tag(name="user-controller", description = "MASTER의 생성/수정/삭제 + 모든 사용자의 권한별 조회 API")
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping()
//    @PreAuthorize("hasRole('MASTER')")
//    @Operation(summary="MASTER의 사용자 생성", description="새로운 사용자를 등록합니다")
    public UserResponseDto registerUser(@RequestBody UserCreateRequestDto requestDto) {
        UserResponseDto responseDto = userService.createUser(requestDto);

        return responseDto;
    }

    @GetMapping("/{userId}")
//    @PreAuthorize("hasRole('MASTER')")
//    @Operation(summary="MASTER의 특정 사용자 조회", description="사용자를 조회합니다")
    public UserResponseDto findUser(@PathVariable("userId") Long userId) {
        UserResponseDto responseDto = userService.getUser(userId);

        return responseDto;
    }

    @GetMapping()
//    @PreAuthorize("hasRole('MASTER')")
//    @Operation(summary="MASTER의 사용자 전체 조회 및 검색", description="검색 키워드로 사용자를 조회합니다 (전체 조회 포함)")
    public List<UserResponseDto> searchUser(
//            @Parameter(description = "사용자 이름, 사용자 소속 그룹(허브 또는 업체)명, 권한으로 검색 가능")
            @RequestParam(required = false) String search) {
        List<UserResponseDto> responseDtos = userService.getUsers(search);

        return responseDtos;
    }


}
