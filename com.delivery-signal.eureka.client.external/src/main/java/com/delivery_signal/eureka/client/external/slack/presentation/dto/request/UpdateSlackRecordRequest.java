package com.delivery_signal.eureka.client.external.slack.presentation.dto.request;

import com.delivery_signal.eureka.client.external.slack.application.dto.request.UpdateSlackRecordRequestDto;
import lombok.Getter;

@Getter
public class UpdateSlackRecordRequest {
    private String recipientId;
    private String message;

    public UpdateSlackRecordRequestDto toDto(){
        return UpdateSlackRecordRequestDto.builder()
                .recipientId(this.recipientId)
                .message(this.message)
                .build();
    }
}
