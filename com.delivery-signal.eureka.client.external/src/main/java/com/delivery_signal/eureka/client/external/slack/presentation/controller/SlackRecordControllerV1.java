package com.delivery_signal.eureka.client.external.slack.presentation.controller;

import com.delivery_signal.eureka.client.external.global.response.CommonApiResponse;
import com.delivery_signal.eureka.client.external.slack.application.service.SlackRecordServiceV1;
import com.delivery_signal.eureka.client.external.slack.presentation.dto.CreateSlackRecordResponse;
import com.delivery_signal.eureka.client.external.slack.presentation.dto.SlackRecordResponse;
import com.delivery_signal.eureka.client.external.slack.presentation.dto.UpdateSlackRecordResponse;
import com.delivery_signal.eureka.client.external.slack.presentation.dto.request.CreateSlackRecordRequest;
import com.delivery_signal.eureka.client.external.slack.presentation.dto.request.UpdateSlackRecordRequest;
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
        SlackRecordResponse response = SlackRecordResponse.from(
                serviceV1.getSlackRecord(id)
        );
        return CommonApiResponse.ok(response);
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

}
