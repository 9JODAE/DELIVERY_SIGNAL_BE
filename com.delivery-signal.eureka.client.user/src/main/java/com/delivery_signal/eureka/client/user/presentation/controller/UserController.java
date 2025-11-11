package com.delivery_signal.eureka.client.user.presentation.controller;

import com.delivery_signal.eureka.client.user.application.command.CheckUserRoleCommand;
import com.delivery_signal.eureka.client.user.application.command.CreateUserCommand;
import com.delivery_signal.eureka.client.user.presentation.dto.request.CreateUserRequest;
import com.delivery_signal.eureka.client.user.presentation.dto.request.UpdateUserApprovalStatusRequest;
import com.delivery_signal.eureka.client.user.presentation.dto.request.UpdateUserRequest;
import com.delivery_signal.eureka.client.user.presentation.dto.request.CheckUserRoleRequest;
import com.delivery_signal.eureka.client.user.presentation.dto.ApiResponse;
import com.delivery_signal.eureka.client.user.presentation.dto.response.GetUserAuthorizationResponse;
import com.delivery_signal.eureka.client.user.presentation.dto.response.GetUserResponse;
import com.delivery_signal.eureka.client.user.application.dto.ApprovalStatusType;
import com.delivery_signal.eureka.client.user.application.dto.UserRoleType;
import com.delivery_signal.eureka.client.user.application.service.UserService;

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
//    @GetMapping("/call")
//    public String callUserByOrder() {
//        return "Order 어플리케이션에서 User의 /call 호출";
//    }

    @GetMapping("/authorization")
    @Operation(summary="다른 애플리케이션의 인가 확인", description="인가를 확인합니다")
//  UserController에서 구현 -> JwtAuthorizationFilter에서 구현 -> Gateway에서 구현됨
    public ResponseEntity<ApiResponse<GetUserAuthorizationResponse>> confirmAuthorization(@RequestHeader("x-user-id") String x_user_id, @RequestBody CheckUserRoleRequest req) {
        // 로그인한 사용자의 권한 확인 아닌 요청 body의 사용자(userId)에 대한 권한 확인
        CheckUserRoleCommand command = CheckUserRoleCommand.of(req.userId(), req.role());
        Boolean check = userService.checkAuthorization(command);
        if (!check) {
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.message("권한이 일치하지 않습니다"));
        }
        String extraInfo = null;
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(new GetUserAuthorizationResponse(req.userId(), req.role(), extraInfo)));

    }

    @GetMapping("/profile")
    @Operation(summary="사용자의 본인 프로필 조회", description="본인의 정보를 조회합니다")
    public ResponseEntity<ApiResponse<GetUserResponse>> getProfile(@RequestHeader("x-user-id") String x_user_id) {
        GetUserResponse res = userService.getUser(Long.parseLong(x_user_id));
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(res));
    }

    @PostMapping()
//    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary="MASTER의 사용자 생성", description="새로운 사용자를 등록합니다")
    public ResponseEntity<ApiResponse<GetUserResponse>> registerUser(@RequestHeader("x-user-id") String x_user_id, @RequestBody CreateUserRequest req) {

        if (!isMaster(Long.parseLong(x_user_id))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.message("사용자 등록은 MASTER만 가능합니다"));
        }
        CreateUserCommand command = CreateUserCommand.of(req.username(), req.password(), req.slackId(), req.organization(), req.organizationId(), req.role(), req.isMaster(), req.masterToken());

        GetUserResponse res = userService.createUser(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(res));
    }

    @GetMapping("/{userId}")
//    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary="MASTER의 특정 사용자 조회", description="사용자를 조회합니다")
    public ResponseEntity<ApiResponse<GetUserResponse>> findUser(@RequestHeader("x-user-id") String x_user_id, @PathVariable("userId") Long userId) {
        /*
        if (!isMaster(Long.parseLong(x_user_id))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.message("특정 사용자 조회는 MASTER만 가능합니다"));
        }

        GetUserResponse responseDto = userService.getUser(userId);

        if (responseDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.message("사용자가 존재하지 않습니다"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(responseDto));
        */




        // dummy data
        if (!isMaster(Long.parseLong(x_user_id))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.message("특정 사용자 조회는 MASTER만 가능합니다"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(new GetUserResponse(
                2L,
                "kimhubgwan",
                "UKIMHUBGWAN",
                "서울특별시 센터 허브",
                UUID.randomUUID(),
                UserRoleType.HUB_MANAGER,
                ApprovalStatusType.APPROVED
        )));
    }

    @GetMapping()
//    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary="MASTER의 사용자 전체 조회 및 검색", description="검색 키워드로 사용자를 조회합니다 (전체 조회 포함)")
    public ResponseEntity<ApiResponse<List<GetUserResponse>>> searchUser(
            @RequestHeader("x-user-id") String x_user_id,
//            @Parameter(description = "사용자 이름, 사용자 소속 그룹(허브 또는 업체)명, 권한으로 검색 가능")
            @RequestParam(required = false) String search) {
        /*
        if (!isMaster(Long.parseLong(x_user_id))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.message("사용자 전체 조회는 MASTER만 가능합니다"));
        }

        List<GetUserResponse> responseDtos = userService.getUsers(search);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(responseDto));

        */



        // dummy data

        if (!isMaster(Long.parseLong(x_user_id))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.message("사용자 전체 조회는 MASTER만 가능합니다"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(List.of(
                new GetUserResponse(1L, "choigoim", "UCHOIGOIM", "delivery-signal", null, UserRoleType.MASTER, ApprovalStatusType.PENDING),

                // 허브 관리자
                new GetUserResponse(2L, "kimhubgwan", "UKIMHUBGWAN", "서울특별시 센터 허브", UUID.randomUUID(), UserRoleType.HUB_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(3L, "leehubgwan", "ULEEHUBGWAN", "세종특별자치시 센터 허브", UUID.randomUUID(), UserRoleType.HUB_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(4L, "sohubgwan", "USOHUBGWAN", "경기 남부 센터 허브", UUID.randomUUID(), UserRoleType.HUB_MANAGER, ApprovalStatusType.PENDING),

                // 배송 관리자 (허브)
                new GetUserResponse(5L, "jeonbaehub", "UJEONBAEHUB", "delivery-signal", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(6L, "yubaehub", "UYUBAEHUB", "delivery-signal", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(7L, "minbaehub", "UMINBAEHUB", "delivery-signal", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(8L, "kobaehub", "UKOBAEHUB", "delivery-signal", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(9L, "kimbaehub", "UKIMBAEHUB", "delivery-signal", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(10L, "parkbaehub", "UPARKBAEHUB", "delivery-signal", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(11L, "jinbaehub", "UJINBAEHUB", "delivery-signal", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(12L, "yangbaehub", "UYANGBAEHUB", "delivery-signal", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(13L, "rhabaehub", "URHABAEHUB", "delivery-signal", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(14L, "woobaehub", "UWOOBAEHUB", "delivery-signal", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),

                // 업체 배송 관리자 - 서울특별시 센터 허브
                new GetUserResponse(15L, "nabaecom", "UNABAECOM", "서울특별자치시 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(16L, "limbaecom", "ULIMBAECOM", "서울특별자치시 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(17L, "jungbaecom", "UJUNGBAECOM", "서울특별자치시 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(18L, "hanbaecom", "UHANBAECOM", "서울특별자치시 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(19L, "ohbaecom", "UOHBAECOM", "서울특별자치시 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(20L, "seobaecom", "USEOBAECOM", "서울특별자치시 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(21L, "hwangbaecom", "UHWANGBAECOM", "서울특별자치시 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(22L, "ahnbaecom", "UAHNBAECOM", "서울특별자치시 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(23L, "songbaecom", "USONGBAECOM", "서울특별자치시 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(24L, "hongbaecom", "UHONGBAECOM", "서울특별자치시 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),

                // 업체 배송 관리자 - 세종특별자치시 센터 허브
                new GetUserResponse(25L, "simbaecom", "USIMBAECOM", "세종특별자치시 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(26L, "nobaecom", "UNOBAECOM", "세종특별자치시 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(27L, "habaecom", "UHABAECOM", "세종특별자치시 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(28L, "sungbaecom", "USUNGBAECOM", "세종특별자치시 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(29L, "chabaecom", "UCHABAECOM", "세종특별자치시 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(30L, "joobaecom", "UJOOBAECOM", "세종특별자치시 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(31L, "munbaecom", "UMUNBAECOM", "세종특별자치시 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(32L, "sonbaecom", "USONBAECOM", "세종특별자치시 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(33L, "yeonbaecom", "UYEONBAECOM", "세종특별자치시 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(34L, "bangbaecom", "UBANGBAECOM", "세종특별자치시 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),

                // 업체 배송 관리자 - 경기 남부 센터 허브
                new GetUserResponse(35L, "jibaecom", "UJIBAECOM", "경기 남부 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(36L, "kibaecom", "UKIBAECOM", "경기 남부 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(37L, "pyobaecom", "UPYOBAECOM", "경기 남부 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(38L, "gubaecom", "UGUBAECOM", "경기 남부 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(39L, "myeongbaecom", "UMYEONGBAECOM", "경기 남부 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(40L, "wangbaecom", "UWANGBAECOM", "경기 남부 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(41L, "dobaecom", "UDOBAECOM", "경기 남부 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(42L, "mabaecom", "UMABAECOM", "경기 남부 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(43L, "jebaecom", "UJEBAECOM", "경기 남부 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(44L, "mobaecom", "UMOBAECOM", "경기 남부 센터 허브", UUID.randomUUID(), UserRoleType.DELIVERY_MANAGER, ApprovalStatusType.PENDING),

                // 업체 관리자
                new GetUserResponse(45L, "yooncomgwan", "UYOONCOMGWAN", "A 업체", UUID.randomUUID(), UserRoleType.SUPPLIER_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(46L, "gilcomgwan", "UGILCOMGWAN", "B 업체", UUID.randomUUID(), UserRoleType.SUPPLIER_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(47L, "euncomgwan", "UEUNCOMGWAN", "C 업체", UUID.randomUUID(), UserRoleType.SUPPLIER_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(48L, "okgwan", "UOKCOMGWAN", "D 업체", UUID.randomUUID(), UserRoleType.SUPPLIER_MANAGER, ApprovalStatusType.PENDING),
                new GetUserResponse(49L, "takgwan", "UTAKCOMGWAN", "E 업체", UUID.randomUUID(), UserRoleType.SUPPLIER_MANAGER, ApprovalStatusType.PENDING)
        )));

    }



    @PatchMapping("/{userId}/approval")
//    @PreAuthorize("hasAnyRole('MASTER, HUB_MANAGER')")
    @Operation(summary="MASTER, Hub Manager의 사용자 회원가입 승인", description="사용자의 회원가입을 승인합니다")
    public ResponseEntity<ApiResponse<Void>> permitUser(@RequestHeader("x-user-id") String x_user_id, @RequestBody UpdateUserApprovalStatusRequest requestDto, @PathVariable("userId") Long userId) {

        if (!(isMaster(Long.parseLong(x_user_id)) || isHubManager(Long.parseLong(x_user_id)))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.message("사용자의 회원가입 승인은 MASTER 또는 HUB_MANAGER만 가능합니다"));
        }

        GetUserResponse responseDto = userService.updateApprovalStatus(userId, requestDto);

        if (responseDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.message("사용자가 존재하지 않습니다"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(null));

    }


    @PutMapping("/{userId}")
//    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary="MASTER의 사용자 정보 수정", description="사용자의 정보를 수정합니다")
    public ResponseEntity<ApiResponse<GetUserResponse>> modifyUserInfo (@RequestHeader("x-user-id") String x_user_id, @RequestBody UpdateUserRequest requestDto, @PathVariable("userId") Long userId) {

        if (!isMaster(Long.parseLong(x_user_id))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.message("사용자 정보 수정은 MASTER만 가능합니다"));
        }

        GetUserResponse responseDto = userService.updateUser(userId, requestDto);

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
        CheckUserRoleCommand command = CheckUserRoleCommand.of(userId, UserRoleType.MASTER);
        return userService.checkAuthorization(command);

    }

    // Hub Manager 여부 확인
    private Boolean isHubManager(Long userId) {
        CheckUserRoleCommand command = CheckUserRoleCommand.of(userId, UserRoleType.HUB_MANAGER);
        return userService.checkAuthorization(command);

    }




}
