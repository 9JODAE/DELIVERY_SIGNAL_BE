package com.delivery_signal.eureka.client.external.slack.presentation.controller;

import com.delivery_signal.eureka.client.external.slack.application.dto.ApiResponse;
import com.delivery_signal.eureka.client.external.slack.application.dto.PageResponse;
import com.delivery_signal.eureka.client.external.slack.presentation.dto.CreateSlackRecordResponse;
import com.delivery_signal.eureka.client.external.slack.presentation.dto.DeleteSlackRecordResponse;
import com.delivery_signal.eureka.client.external.slack.presentation.dto.SlackRecordResponse;
import com.delivery_signal.eureka.client.external.slack.presentation.dto.UpdateSlackRecordResponse;
import com.delivery_signal.eureka.client.external.slack.presentation.dto.request.CreateSlackMessageRequest;
import com.delivery_signal.eureka.client.external.slack.presentation.dto.request.CreateSlackRecordRequest;
import com.delivery_signal.eureka.client.external.slack.presentation.dto.request.UpdateSlackRecordRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// 💡 @Tag 어노테이션은 클래스 레벨에서 분리합니다.
@Tag(name = "Slack 기록 및 메시징", description = "Slack 메시지 기록 관리 및 전송 관련 API")
public interface SlackRecordApiV1 {

    // 1. Slack 기록 생성 (POST /)
    @Operation(summary = "Slack 기록 생성", description = "특정 수신자에게 보낼 Slack 메시지 기록을 생성합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Slack 기록 생성 성공")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음")
    @PostMapping
    ResponseEntity<ApiResponse<CreateSlackRecordResponse>> createSlackRecord(
            @RequestBody CreateSlackRecordRequest request,
            @Parameter(description = "요청을 수행하는 사용자 ID (권한 확인용)")
            @RequestHeader("x-user-id") Long userId
    );

    // 2. Slack 기록 단건 조회 (GET /{id})
    @Operation(summary = "Slack 기록 단건 조회", description = "특정 ID의 Slack 기록을 조회합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Slack 기록 조회 성공")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 Slack 기록")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음")
    @GetMapping("/{id}")
    ResponseEntity<ApiResponse<SlackRecordResponse>> getSlackRecord(
            @Parameter(description = "조회할 Slack 기록의 UUID")
            @PathVariable UUID id,
            @Parameter(description = "요청을 수행하는 사용자 ID (권한 확인용)")
            @RequestHeader("x-user-id") Long userId
    );

    // 3. Slack 기록 리스트 조회 (GET /)
    @Operation(summary = "Slack 기록 리스트 조회 (페이지네이션)", description = "Slack 기록 목록을 페이지 단위로 조회합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Slack 기록 리스트 조회 성공")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음")
    @GetMapping
    ResponseEntity<ApiResponse<PageResponse<SlackRecordResponse>>> getSlackRecordList(
            @Parameter(description = "요청을 수행하는 사용자 ID (권한 확인용)")
            @RequestHeader("x-user-id") Long userId,
            @Parameter(description = "조회할 페이지 번호 (기본값: 1)")
            @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(description = "한 페이지당 항목 개수 (기본값: 10)")
            @RequestParam(value = "size", defaultValue = "10") int size,
            @Parameter(description = "정렬 기준 필드 (기본값: createdAt)")
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @Parameter(description = "오름차순 정렬 여부 (true: ASC, false: DESC)")
            @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc
    );

    // 4. Slack 기록 수정 (PATCH /{id})
    @Operation(summary = "Slack 기록 수정", description = "특정 ID의 Slack 기록을 수정합니다. (메시지 내용 등)")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Slack 기록 수정 성공")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 Slack 기록")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음")
    @PatchMapping("/{id}")
    ResponseEntity<ApiResponse<UpdateSlackRecordResponse>> updateSlackRecord(
            @Parameter(description = "수정할 Slack 기록의 UUID")
            @PathVariable UUID id,
            @RequestBody UpdateSlackRecordRequest request,
            @Parameter(description = "요청을 수행하는 사용자 ID (권한 확인용)")
            @RequestHeader("x-user-id") Long userId
    );

    // 5. Slack 기록 삭제 (DELETE /{id})
    @Operation(summary = "Slack 기록 삭제 (Soft Delete)", description = "특정 ID의 Slack 기록을 논리적으로 삭제 처리합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Slack 기록 삭제 성공")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 Slack 기록")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음")
    @DeleteMapping("/{id}")
    ResponseEntity<ApiResponse<DeleteSlackRecordResponse>> softDeleteSlackRecord(
            @Parameter(description = "삭제할 Slack 기록의 UUID")
            @PathVariable UUID id,
            @Parameter(description = "요청을 수행하는 사용자 ID (권한 확인용)")
            @RequestHeader("x-user-id") Long userId
    );

    // 6. Slack 메시지 전송 테스트 (POST /message/test)
    @Operation(summary = "Slack 메시지 전송 테스트", description = "특정 Slack User ID로 DM을 즉시 전송하는 테스트 API.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "메시지 전송 요청 성공 (실패 시 에러 메시지 반환)")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버/API 오류 발생")
    @PostMapping("/message/test")
    ResponseEntity<ApiResponse<String>> sendSlackMessageTest(
            @Parameter(description = "메시지를 받을 Slack User ID (예: U0XXXXXXX)")
            @RequestParam String slackUserId,
            @Parameter(description = "전송할 메시지 내용")
            @RequestParam String message
    );

    // 7. Slack 알림 메시지 전송 (POST /message)
    @Operation(summary = "Slack 알림 메시지 전송", description = "Slack 기록을 기반으로 알림 메시지를 전송합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "메시지 전송 요청 성공")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버/API 오류 발생")
    @PostMapping("/message")
    ResponseEntity<ApiResponse<String>> sendSlackMessage(
            @RequestBody CreateSlackMessageRequest request,
            @Parameter(description = "요청을 수행하는 사용자 ID (권한 확인용)")
            @RequestHeader("x-user-id") Long userId
    );
}