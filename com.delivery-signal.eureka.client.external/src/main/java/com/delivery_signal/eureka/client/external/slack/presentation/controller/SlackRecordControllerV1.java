package com.delivery_signal.eureka.client.external.slack.presentation.controller;

import com.delivery_signal.eureka.client.external.global.response.CommonApiResponse;
import com.delivery_signal.eureka.client.external.slack.application.service.SlackRecordServiceV1;
import com.delivery_signal.eureka.client.external.slack.presentation.dto.CreateSlackRecordResponse;
import com.delivery_signal.eureka.client.external.slack.presentation.dto.SlackRecordResponse;
import com.delivery_signal.eureka.client.external.slack.presentation.dto.request.CreateSlackRecordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/slack")
@RequiredArgsConstructor
public class SlackRecordControllerV1 {

    private final SlackRecordServiceV1 serviceV1;

    @PostMapping
    public ResponseEntity<CommonApiResponse<CreateSlackRecordResponse>> createSlackRecord(
            @RequestBody CreateSlackRecordRequest request
            ) {
        CreateSlackRecordResponse response = CreateSlackRecordResponse.from(
                serviceV1.createSlackRecord(request.toDto())
        );
        return CommonApiResponse.created(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonApiResponse<SlackRecordResponse>> getSlackRecord(
            @PathVariable UUID id
            ){
        return null;
    }

}
