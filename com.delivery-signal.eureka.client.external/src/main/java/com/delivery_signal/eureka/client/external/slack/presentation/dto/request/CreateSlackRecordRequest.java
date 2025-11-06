package com.delivery_signal.eureka.client.external.slack.presentation.dto.request;

import com.delivery_signal.eureka.client.external.slack.application.dto.request.CreateSlackRecordRequestDto;
import lombok.Getter;

@Getter
public class CreateSlackRecordRequest {
    private String recipientId;
    private String message;

    public CreateSlackRecordRequestDto toDto(){
        return CreateSlackRecordRequestDto.builder()
                .recipientId(this.recipientId)
                .message(this.message)
                .build();
    }
}
