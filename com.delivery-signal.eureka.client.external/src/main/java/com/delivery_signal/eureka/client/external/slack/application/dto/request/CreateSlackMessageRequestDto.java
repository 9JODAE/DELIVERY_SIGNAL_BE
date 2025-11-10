package com.delivery_signal.eureka.client.external.slack.application.dto.request;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class CreateSlackMessageRequestDto {

    private String slackUserId;
    private String orderNum;
    private String orderUserInfo;
    private String orderTime;
    private String productInfo;
    private String detailRequest;
    private String origin;
    private String layOver;
    private String destination;
    private String deliveryUserInfo;
}
