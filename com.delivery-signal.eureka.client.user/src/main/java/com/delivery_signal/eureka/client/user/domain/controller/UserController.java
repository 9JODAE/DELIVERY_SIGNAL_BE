package com.delivery_signal.eureka.client.user.domain.controller;

import com.delivery_signal.eureka.client.user.domain.dto.request.UserCreateRequestDto;
import com.delivery_signal.eureka.client.user.domain.dto.response.UserResponseDto;
import com.delivery_signal.eureka.client.user.domain.entity.ApprovalStatus;
import com.delivery_signal.eureka.client.user.domain.entity.UserRole;
import com.delivery_signal.eureka.client.user.domain.service.UserService;
import com.delivery_signal.eureka.client.user.presentation.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
//@Tag(name="domain-controller", description = "MASTER의 생성/수정/삭제 + 모든 사용자의 권한별 조회 API")
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;


    // Feign Client
    @GetMapping("/call")
    public String callOrderByUser() {
        return userService.callOrder();
    }


    @PostMapping()
//    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary="MASTER의 사용자 생성", description="새로운 사용자를 등록합니다")
    public ResponseEntity<ApiResponse<UserResponseDto>> registerUser(@RequestBody UserCreateRequestDto requestDto) {
        UserResponseDto responseDto = userService.createUser(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(responseDto));
    }

    @GetMapping("/{userId}")
//    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary="MASTER의 특정 사용자 조회", description="사용자를 조회합니다")
    public UserResponseDto findUser(@PathVariable("userId") Long userId) {
        /*
        UserResponseDto responseDto = userService.getUser(userId);

        return responseDto;

        */

        // dummy data
        return new UserResponseDto(
                2L,
                "kimhubgwan",
                "UKIMHUBGWAN",
                "서울특별시 센터 허브",
                UserRole.HUB_MANAGER,
                ApprovalStatus.APPROVED
        );
    }

    @GetMapping()
//    @PreAuthorize("hasRole('MASTER')")
    @Operation(summary="MASTER의 사용자 전체 조회 및 검색", description="검색 키워드로 사용자를 조회합니다 (전체 조회 포함)")
    public List<UserResponseDto> searchUser(
//            @Parameter(description = "사용자 이름, 사용자 소속 그룹(허브 또는 업체)명, 권한으로 검색 가능")
            @RequestParam(required = false) String search) {
        /*
        List<UserResponseDto> responseDtos = userService.getUsers(search);

        return responseDtos;

        */

        // dummy data
        return List.of(
                new UserResponseDto(1L, "choigoim", "UCHOIGOIM", "delivery-signal", UserRole.MASTER, ApprovalStatus.PENDING),

                // 허브 관리자
                new UserResponseDto(2L, "kimhubgwan", "UKIMHUBGWAN", "서울특별시 센터 허브", UserRole.HUB_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(3L, "leehubgwan", "ULEEHUBGWAN", "세종특별자치시 센터 허브", UserRole.HUB_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(4L, "sohubgwan", "USOHUBGWAN", "경기 남부 센터 허브", UserRole.HUB_MANAGER, ApprovalStatus.PENDING),

                // 배송 관리자 (허브)
                new UserResponseDto(5L, "jeonbaehub", "UJEONBAEHUB", "delivery-signal", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(6L, "yubaehub", "UYUBAEHUB", "delivery-signal", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(7L, "minbaehub", "UMINBAEHUB", "delivery-signal", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(8L, "kobaehub", "UKOBAEHUB", "delivery-signal", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(9L, "kimbaehub", "UKIMBAEHUB", "delivery-signal", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(10L, "parkbaehub", "UPARKBAEHUB", "delivery-signal", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(11L, "jinbaehub", "UJINBAEHUB", "delivery-signal", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(12L, "yangbaehub", "UYANGBAEHUB", "delivery-signal", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(13L, "rhabaehub", "URHABAEHUB", "delivery-signal", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(14L, "woobaehub", "UWOOBAEHUB", "delivery-signal", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),

                // 업체 배송 관리자 - 서울특별시 센터 허브
                new UserResponseDto(15L, "nabaecom", "UNABAECOM", "서울특별자치시 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(16L, "limbaecom", "ULIMBAECOM", "서울특별자치시 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(17L, "jungbaecom", "UJUNGBAECOM", "서울특별자치시 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(18L, "hanbaecom", "UHANBAECOM", "서울특별자치시 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(19L, "ohbaecom", "UOHBAECOM", "서울특별자치시 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(20L, "seobaecom", "USEOBAECOM", "서울특별자치시 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(21L, "hwangbaecom", "UHWANGBAECOM", "서울특별자치시 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(22L, "ahnbaecom", "UAHNBAECOM", "서울특별자치시 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(23L, "songbaecom", "USONGBAECOM", "서울특별자치시 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(24L, "hongbaecom", "UHONGBAECOM", "서울특별자치시 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),

                // 업체 배송 관리자 - 세종특별자치시 센터 허브
                new UserResponseDto(25L, "simbaecom", "USIMBAECOM", "세종특별자치시 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(26L, "nobaecom", "UNOBAECOM", "세종특별자치시 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(27L, "habaecom", "UHABAECOM", "세종특별자치시 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(28L, "sungbaecom", "USUNGBAECOM", "세종특별자치시 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(29L, "chabaecom", "UCHABAECOM", "세종특별자치시 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(30L, "joobaecom", "UJOOBAECOM", "세종특별자치시 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(31L, "munbaecom", "UMUNBAECOM", "세종특별자치시 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(32L, "sonbaecom", "USONBAECOM", "세종특별자치시 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(33L, "yeonbaecom", "UYEONBAECOM", "세종특별자치시 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(34L, "bangbaecom", "UBANGBAECOM", "세종특별자치시 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),

                // 업체 배송 관리자 - 경기 남부 센터 허브
                new UserResponseDto(35L, "jibaecom", "UJIBAECOM", "경기 남부 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(36L, "kibaecom", "UKIBAECOM", "경기 남부 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(37L, "pyobaecom", "UPYOBAECOM", "경기 남부 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(38L, "gubaecom", "UGUBAECOM", "경기 남부 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(39L, "myeongbaecom", "UMYEONGBAECOM", "경기 남부 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(40L, "wangbaecom", "UWANGBAECOM", "경기 남부 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(41L, "dobaecom", "UDOBAECOM", "경기 남부 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(42L, "mabaecom", "UMABAECOM", "경기 남부 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(43L, "jebaecom", "UJEBAECOM", "경기 남부 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(44L, "mobaecom", "UMOBAECOM", "경기 남부 센터 허브", UserRole.DELIVERY_MANAGER, ApprovalStatus.PENDING),

                // 업체 관리자
                new UserResponseDto(45L, "yooncomgwan", "UYOONCOMGWAN", "A 업체", UserRole.SUPPLIER_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(46L, "gilcomgwan", "UGILCOMGWAN", "B 업체", UserRole.SUPPLIER_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(47L, "euncomgwan", "UEUNCOMGWAN", "C 업체", UserRole.SUPPLIER_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(48L, "okgwan", "UOKCOMGWAN", "D 업체", UserRole.SUPPLIER_MANAGER, ApprovalStatus.PENDING),
                new UserResponseDto(49L, "takgwan", "UTAKCOMGWAN", "E 업체", UserRole.SUPPLIER_MANAGER, ApprovalStatus.PENDING)

        );
    }





}
