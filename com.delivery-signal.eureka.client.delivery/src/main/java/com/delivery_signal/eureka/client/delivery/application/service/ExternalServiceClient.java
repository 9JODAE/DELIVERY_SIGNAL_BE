package com.delivery_signal.eureka.client.delivery.application.service;


import com.delivery_signal.eureka.client.delivery.presentation.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "hub-service", path = "${internal.external.url}")
public interface ExternalServiceClient {

    @GetMapping("/open-api/v1/externals/slacks/message")
    ApiResponse<String> sendSlackMessage(
        @RequestBody CreateSlackMessageRequest request);

    record CreateSlackMessageRequest(
        String slackUserId,
        String orderNum,
        String orderUserInfo,
        String orderTime,
        String productInfo,
        String detailRequest,
        String origin,
        String layOver,
        String destination,
        String deliveryUserInfo
    ) {}
}
