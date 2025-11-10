package com.delivery_signal.eureka.client.external.slack.presentation.controller;

import com.delivery_signal.eureka.client.external.global.response.CommonApiResponse;
import com.delivery_signal.eureka.client.external.global.response.PageResponse;
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
    public ResponseEntity<CommonApiResponse<CreateSlackRecordResponse>> createSlackRecord(
            @RequestBody CreateSlackRecordRequest request
            ) {
        CreateSlackRecordResponse response = CreateSlackRecordResponse.from(
                serviceV1.createSlackRecord(request.getRecipientId(),request.getMessage())
        );
        return CommonApiResponse.created(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonApiResponse<SlackRecordResponse>> getSlackRecord(
            @PathVariable UUID id
            ){
        SlackRecordResponse response = SlackRecordResponse.from(
                serviceV1.getSlackRecord(id)
        );
        return CommonApiResponse.ok(response);
    }

    @GetMapping
    public ResponseEntity<CommonApiResponse<PageResponse<SlackRecordResponse>>> getSlackRecordList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc
            ) {
        Page<SlackRecordDto> slackRecordDtoPage = serviceV1.getSlackRecordList(page, size, sortBy, isAsc);
        Page<SlackRecordResponse> slackRecordResponsePage = slackRecordDtoPage.map(SlackRecordResponse::from);
        return CommonApiResponse.ok(PageResponse.fromPage(slackRecordResponsePage));
    }



    @PatchMapping("/{id}")
    public ResponseEntity<CommonApiResponse<UpdateSlackRecordResponse>> updateSlackRecord(
            @PathVariable UUID id,
            @RequestBody UpdateSlackRecordRequest request
            ){
        UpdateSlackRecordResponse response = UpdateSlackRecordResponse.from(
                serviceV1.updateSlackRecord(id,request.toDto())
        );
        return CommonApiResponse.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonApiResponse<DeleteSlackRecordResponse>> softDeleteSlackRecord(
            @PathVariable UUID id
            ){
        DeleteSlackRecordResponse response = DeleteSlackRecordResponse.from(
                serviceV1.softDeleteSlackRecord(id)
        );
        return CommonApiResponse.ok(response);
    }

    @PostMapping("/message/test")
    public ResponseEntity<CommonApiResponse<String>> sendSlackMessageTest(
            @RequestParam String slackUserId,
            @RequestParam String message){
        return CommonApiResponse.ok(messageServiceV1.slackMessageSend(slackUserId,message));
    }

    @PostMapping("/message")
    public ResponseEntity<CommonApiResponse<String>> sendSlackMessage(
            @RequestBody CreateSlackMessageRequest request
            ){
        return CommonApiResponse.ok(messageServiceV1.notificationMessageSend(request.toDto()));
    }


}
