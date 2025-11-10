package com.delivery_signal.eureka.client.user.presentation.controller;

import com.delivery_signal.eureka.client.user.presentation.dto.request.UserCreateRequestDto;
import com.delivery_signal.eureka.client.user.presentation.dto.request.UserUpdateApprovalStatusRequestDto;
import com.delivery_signal.eureka.client.user.presentation.dto.request.UserUpdateRequestDto;
import com.delivery_signal.eureka.client.user.presentation.dto.request.UserRoleCheckRequestDto;
import com.delivery_signal.eureka.client.user.presentation.dto.response.UserAuthorizationResponseDto;
import com.delivery_signal.eureka.client.user.presentation.dto.response.UserResponseDto;
import com.delivery_signal.eureka.client.user.domain.model.ApprovalStatus;
import com.delivery_signal.eureka.client.user.domain.model.UserRole;
import com.delivery_signal.eureka.client.user.application.service.UserService;
import com.delivery_signal.eureka.client.user.presentation.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Slf4j
@RestController
@RequiredArgsConstructor
//@Tag(name="user-controller", description = "MASTER의 생성/수정/삭제 + 모든 사용자의 권한별 조회 API")
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    // Feign Client
    @GetMapping("/call")
    public String callUserByOrder() {
        return "Order 어플리케이션에서 User의 /call 호출";
    }

    @GetMapping("/authorization")
    @Operation(summary="다른 애플리케이션의 인가 확인", description="인가를 확인합니다")
//  JwtAuthorizationFilter에서 구현 -> Gateway에서 구현됨
    public ResponseEntity<ApiResponse<UserAuthorizationResponseDto>> confirmAuthorization(@RequestHeader("x-user-id") String x_user_id, @RequestBody UserRoleCheckRequestDto requestDto) {
        // 로그인한 사용자의 권한 확인 아닌 요청 body의 사용자(userId)에 대한 권한 확인
        Boolean check = userService.checkAuthorization(requestDto);
        if (!check) {
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.message("권한이 일치하지 않습니다"));
        }
        String extraInfo = null;
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(new UserAuthorizationResponseDto(requestDto.userId(),requestDto.role(), extraInfo)));

    }

    @GetMapping("/profile")
    @Operation(summary="사용자의 본인 프로필 조회", description="본인의 정보를 조회합니다")
    public ResponseEntity<ApiResponse<UserResponseDto>> getProfile(@RequestHeader("x-user-id") String x_user_id) {
        UserResponseDto responseDto = userService.getUser(Long.parseLong(x_user_id));
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(responseDto));
    }

    @PostMapping()
//    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary="MASTER의 사용자 생성", description="새로운 사용자를 등록합니다")
    public ResponseEntity<ApiResponse<UserResponseDto>> registerUser(@RequestHeader("x-user-id") String x_user_id, @RequestBody UserCreateRequestDto requestDto) {

        if (!isMaster(Long.parseLong(x_user_id))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.message("사용자 등록은 MASTER만 가능합니다"));
        }
        UserResponseDto responseDto = userService.createUser(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(responseDto));
    }

    @GetMapping("/{userId}")
//    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary="MASTER의 특정 사용자 조회", description="사용자를 조회합니다")
    public ResponseEntity<ApiResponse<UserResponseDto>> findUser(@RequestHeader("x-user-id") String x_user_id, @PathVariable("userId") Long userId) {
        /*
        if (!isMaster(Long.parseLong(x_user_id))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.message("특정 사용자 조회는 MASTER만 가능합니다"));
        }

        UserResponseDto responseDto = userService.getUser(userId);

        if (responseDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.message("사용자가 존재하지 않습니다"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(responseDto));
        */




        // dummy data
        if (!isMaster(Long.parseLong(x_user_id))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.message("특정 사용자 조회는 MASTER만 가능합니다"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(new UserResponseDto(
                2L,
                "kimhubgwan",
                "UKIMHUBGWAN",
                "서울특별시 센터 허브",
                UUID.randomUUID(),
                UserRole.HUB_MANAGER,
                ApprovalStatus.APPROVED
        )));
    }

    @GetMapping()
//    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary="MASTER의 사용자 전체 조회 및 검색", description="검색 키워드로 사용자를 조회합니다 (전체 조회 포함)")
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> searchUser(
            @RequestHeader("x-user-id") String x_user_id,
//            @Parameter(description = "사용자 이름, 사용자 소속 그룹(허브 또는 업체)명, 권한으로 검색 가능")
            @RequestParam(required = false) String search) {
        /*
        if (!isMaster(Long.parseLong(x_user_id))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.message("사용자 전체 조회는 MASTER만 가능합니다"));
        }

        List<UserResponseDto> responseDtos = userService.getUsers(search);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(responseDto));

        */



        // dummy data

        if (!isMaster(Long.parseLong(x_user_id))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.message("사용자 전체 조회는 MASTER만 가능합니다"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(List.of(
                new UserResponseDto(1L, "choigoim", "UCHOIGOIM", "delivery-signal", null, UserRole.MASTER, ApprovalStatus.PENDING),

                // 허브 관리자
                new UserResponseDto(2L, "kimhubgwan", "UKIMHUBGWAN", "서울특별시 센터 허브", UUID.randomUUID(), UserRole.HUB_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(3L, "leehubgwan", "ULEEHUBGWAN", "세종특별자치시 센터 허브", UUID.randomUUID(), UserRole.HUB_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(4L, "sohubgwan", "USOHUBGWAN", "경기 남부 센터 허브", UUID.randomUUID(), UserRole.HUB_MANAGER, ApprovalStatus.PENDING),

                // 배송 관리자 (허브)
                new UserResponseDto(5L, "jeonbaehub", "UJEONBAEHUB", "delivery-signal", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(6L, "yubaehub", "UYUBAEHUB", "delivery-signal", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(7L, "minbaehub", "UMINBAEHUB", "delivery-signal", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(8L, "kobaehub", "UKOBAEHUB", "delivery-signal", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(9L, "kimbaehub", "UKIMBAEHUB", "delivery-signal", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(10L, "parkbaehub", "UPARKBAEHUB", "delivery-signal", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(11L, "jinbaehub", "UJINBAEHUB", "delivery-signal", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(12L, "yangbaehub", "UYANGBAEHUB", "delivery-signal", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(13L, "rhabaehub", "URHABAEHUB", "delivery-signal", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(14L, "woobaehub", "UWOOBAEHUB", "delivery-signal", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),

                // 업체 배송 관리자 - 서울특별시 센터 허브
                new UserResponseDto(15L, "nabaecom", "UNABAECOM", "서울특별자치시 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(16L, "limbaecom", "ULIMBAECOM", "서울특별자치시 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(17L, "jungbaecom", "UJUNGBAECOM", "서울특별자치시 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(18L, "hanbaecom", "UHANBAECOM", "서울특별자치시 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(19L, "ohbaecom", "UOHBAECOM", "서울특별자치시 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(20L, "seobaecom", "USEOBAECOM", "서울특별자치시 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(21L, "hwangbaecom", "UHWANGBAECOM", "서울특별자치시 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(22L, "ahnbaecom", "UAHNBAECOM", "서울특별자치시 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(23L, "songbaecom", "USONGBAECOM", "서울특별자치시 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(24L, "hongbaecom", "UHONGBAECOM", "서울특별자치시 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),

                // 업체 배송 관리자 - 세종특별자치시 센터 허브
                new UserResponseDto(25L, "simbaecom", "USIMBAECOM", "세종특별자치시 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(26L, "nobaecom", "UNOBAECOM", "세종특별자치시 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(27L, "habaecom", "UHABAECOM", "세종특별자치시 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(28L, "sungbaecom", "USUNGBAECOM", "세종특별자치시 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(29L, "chabaecom", "UCHABAECOM", "세종특별자치시 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(30L, "joobaecom", "UJOOBAECOM", "세종특별자치시 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(31L, "munbaecom", "UMUNBAECOM", "세종특별자치시 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(32L, "sonbaecom", "USONBAECOM", "세종특별자치시 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(33L, "yeonbaecom", "UYEONBAECOM", "세종특별자치시 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(34L, "bangbaecom", "UBANGBAECOM", "세종특별자치시 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),

                // 업체 배송 관리자 - 경기 남부 센터 허브
                new UserResponseDto(35L, "jibaecom", "UJIBAECOM", "경기 남부 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(36L, "kibaecom", "UKIBAECOM", "경기 남부 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(37L, "pyobaecom", "UPYOBAECOM", "경기 남부 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(38L, "gubaecom", "UGUBAECOM", "경기 남부 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(39L, "myeongbaecom", "UMYEONGBAECOM", "경기 남부 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(40L, "wangbaecom", "UWANGBAECOM", "경기 남부 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(41L, "dobaecom", "UDOBAECOM", "경기 남부 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(42L, "mabaecom", "UMABAECOM", "경기 남부 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(43L, "jebaecom", "UJEBAECOM", "경기 남부 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(44L, "mobaecom", "UMOBAECOM", "경기 남부 센터 허브", UUID.randomUUID(), UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),

                // 업체 관리자
                new UserResponseDto(45L, "yooncomgwan", "UYOONCOMGWAN", "A 업체", UUID.randomUUID(), UserRole.SUPPLIER_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(46L, "gilcomgwan", "UGILCOMGWAN", "B 업체", UUID.randomUUID(), UserRole.SUPPLIER_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(47L, "euncomgwan", "UEUNCOMGWAN", "C 업체", UUID.randomUUID(), UserRole.SUPPLIER_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(48L, "okgwan", "UOKCOMGWAN", "D 업체", UUID.randomUUID(), UserRole.SUPPLIER_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(49L, "takgwan", "UTAKCOMGWAN", "E 업체", UUID.randomUUID(), UserRole.SUPPLIER_MANAGER, ApprovalStatus.PENDING)
        )));

    }



    @PatchMapping("/{userId}/approval")
//    @PreAuthorize("hasAnyRole('MASTER, HUB_MANAGER')")
    @Operation(summary="MASTER, Hub Manager의 사용자 회원가입 승인", description="사용자의 회원가입을 승인합니다")
    public ResponseEntity<ApiResponse<Void>> permitUser(@RequestHeader("x-user-id") String x_user_id, @RequestBody UserUpdateApprovalStatusRequestDto requestDto, @PathVariable("userId") Long userId) {

        if (!(isMaster(Long.parseLong(x_user_id)) || isHubManager(Long.parseLong(x_user_id)))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.message("사용자의 회원가입 승인은 MASTER 또는 HUB_MANAGER만 가능합니다"));
        }

        UserResponseDto responseDto = userService.updateApprovalStatus(userId, requestDto);

        if (responseDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.message("사용자가 존재하지 않습니다"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));

    }


    @PutMapping("/{userId}")
//    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary="MASTER의 사용자 정보 수정", description="사용자의 정보를 수정합니다")
    public ResponseEntity<ApiResponse<UserResponseDto>> modifyUserInfo (@RequestHeader("x-user-id") String x_user_id, @RequestBody UserUpdateRequestDto requestDto, @PathVariable("userId") Long userId) {

        if (!isMaster(Long.parseLong(x_user_id))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.message("사용자 정보 수정은 MASTER만 가능합니다"));
        }

        UserResponseDto responseDto = userService.updateUser(userId, requestDto);

        if (responseDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.message("사용자가 존재하지 않습니다"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(responseDto));
    }

    @DeleteMapping("/{userId}")
//    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary="MASTER의 사용자 정보 삭제", description="사용자의 정보를 삭제합니다")
    public ResponseEntity<ApiResponse<Void>> deleteUser (@RequestHeader("x-user-id") String x_user_id, @PathVariable("userId") Long userId) {

        if (!isMaster(Long.parseLong(x_user_id))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.message("사용자 정보 삭제는 MASTER만 가능합니다"));
        }

        Boolean deleted = userService.softDeleteUser(userId);

        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.message("사용자가 존재하지 않습니다"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));
    }



    // Master 여부 확인
    private Boolean isMaster(Long userId) {
        return userService.checkAuthorization(new UserRoleCheckRequestDto(userId, UserRole.MASTER));

    }

    // Hub Manager 여부 확인
    private Boolean isHubManager(Long userId) {
        return userService.checkAuthorization(new UserRoleCheckRequestDto(userId, UserRole.HUB_MANAGER));

    }




}
