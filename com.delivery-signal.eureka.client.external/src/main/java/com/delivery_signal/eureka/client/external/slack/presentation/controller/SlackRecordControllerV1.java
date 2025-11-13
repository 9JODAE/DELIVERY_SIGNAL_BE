package com.delivery_signal.eureka.client.external.slack.presentation.controller;

import com.delivery_signal.eureka.client.external.slack.application.dto.ApiResponse;
import com.delivery_signal.eureka.client.external.slack.application.dto.PageResponse;
import com.delivery_signal.eureka.client.external.slack.application.service.SlackMessageServiceV1;
import com.delivery_signal.eureka.client.external.slack.application.dto.SlackRecordDto;
import com.delivery_signal.eureka.client.external.slack.application.service.SlackRecordServiceV1;
import com.delivery_signal.eureka.client.external.slack.presentation.dto.CreateSlackRecordResponse;
import com.delivery_signal.eureka.client.external.slack.presentation.dto.DeleteSlackRecordResponse;
import com.delivery_signal.eureka.client.external.slack.presentation.dto.SlackRecordResponse;
import com.delivery_signal.eureka.client.external.slack.presentation.dto.UpdateSlackRecordResponse;
import com.delivery_signal.eureka.client.external.slack.presentation.dto.request.CreateSlackMessageRequest;
import com.delivery_signal.eureka.client.external.slack.presentation.dto.request.CreateSlackRecordRequest;
import com.delivery_signal.eureka.client.external.slack.presentation.dto.request.UpdateSlackRecordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("open-api/v1/externals/slacks")
@RequiredArgsConstructor
public class SlackRecordControllerV1 {

    private final SlackRecordServiceV1 serviceV1;
    private final SlackMessageServiceV1 messageServiceV1;

    @PostMapping
    public ResponseEntity<ApiResponse<CreateSlackRecordResponse>> createSlackRecord(
            @RequestBody CreateSlackRecordRequest request
            ) {
        CreateSlackRecordResponse response = CreateSlackRecordResponse.from(
                serviceV1.createSlackRecord(request.getRecipientId(),request.getMessage())
        );
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SlackRecordResponse>> getSlackRecord(
            @PathVariable UUID id,
            @RequestHeader("x-user-id") Long userId
            ){
        SlackRecordResponse response = SlackRecordResponse.from(
                serviceV1.getSlackRecord(id)
        );
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<SlackRecordResponse>>> getSlackRecordList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc
            ) {
        Page<SlackRecordDto> slackRecordDtoPage = serviceV1.getSlackRecordList(page, size, sortBy, isAsc);
        Page<SlackRecordResponse> slackRecordResponsePage = slackRecordDtoPage.map(SlackRecordResponse::from);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.fromPage(slackRecordResponsePage)));
    }



    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UpdateSlackRecordResponse>> updateSlackRecord(
            @PathVariable UUID id,
            @RequestBody UpdateSlackRecordRequest request
            ){
        UpdateSlackRecordResponse response = UpdateSlackRecordResponse.from(
                serviceV1.updateSlackRecord(id,request.toDto())
        );
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<DeleteSlackRecordResponse>> softDeleteSlackRecord(
            @PathVariable UUID id
            ){
        DeleteSlackRecordResponse response = DeleteSlackRecordResponse.from(
                serviceV1.softDeleteSlackRecord(id)
        );
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/message/test")
    public ResponseEntity<ApiResponse<String>> sendSlackMessageTest(
            @RequestParam String slackUserId,
            @RequestParam String message){
        return ResponseEntity.ok(ApiResponse.success(messageServiceV1.slackMessageSend(slackUserId,message)));
    }

    @PostMapping("/message")
    public ResponseEntity<ApiResponse<String>> sendSlackMessage(
            @RequestBody CreateSlackMessageRequest request
            ){
        return ResponseEntity.ok(ApiResponse.success(messageServiceV1.notificationMessageSend(request.toDto())));
    }


}
