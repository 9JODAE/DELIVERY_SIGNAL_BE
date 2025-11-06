package com.delivery_signal.eureka.client.external.slack.application.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateSlackRecordRequestDto {

    private String recipientId;
    private String message;
}
