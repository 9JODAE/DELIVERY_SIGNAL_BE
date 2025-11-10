package com.delivery_signal.eureka.client.external.slack.presentation.dto.request;

import com.delivery_signal.eureka.client.external.slack.application.dto.request.CreateSlackMessageRequestDto;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CreateSlackMessageRequest {

    private String slackUserId;
    // 주문번호
    private String orderNum;
    // 주문자 정보
    private String orderUserInfo;
    // 주문시간
    private String orderTime;
    // 상품정보
    private String productInfo;
    // 요청사항
    private String detailRequest;
    // 발송지
    private String origin;
    // 경유지
    private String layOver;
    // 도착지
    private String destination;
    // 배송담당자 정보
    private String deliveryUserInfo;

    public CreateSlackMessageRequestDto toDto(){
        return CreateSlackMessageRequestDto.builder()
                .slackUserId(slackUserId)
                .orderNum(orderNum)
                .orderUserInfo(orderUserInfo)
                .orderTime(orderTime)
                .productInfo(productInfo)
                .detailRequest(detailRequest)
                .origin(origin)
                .layOver(layOver)
                .destination(destination)
                .deliveryUserInfo(deliveryUserInfo)
                .build();
    }
}
