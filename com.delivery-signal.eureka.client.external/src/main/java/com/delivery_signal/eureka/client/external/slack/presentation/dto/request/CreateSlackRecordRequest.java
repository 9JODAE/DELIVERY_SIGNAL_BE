package com.delivery_signal.eureka.client.external.slack.presentation.dto.request;

import lombok.Getter;

@Getter
public class CreateSlackRecordRequest {
    private String recipientId;
    private String message;

}
