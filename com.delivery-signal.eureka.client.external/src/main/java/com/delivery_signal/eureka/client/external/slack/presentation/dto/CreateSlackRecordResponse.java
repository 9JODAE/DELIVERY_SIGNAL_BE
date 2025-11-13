package com.delivery_signal.eureka.client.external.slack.presentation.dto;

import com.delivery_signal.eureka.client.external.slack.application.dto.SlackRecordCreationDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class CreateSlackRecordResponse {

    private final UUID slackRecordId;
    private final String recipientId;
    private final String message;
    private final LocalDateTime createdAt;
    private final Long createdBy;

    public static CreateSlackRecordResponse from(SlackRecordCreationDto dto){
        return CreateSlackRecordResponse.builder()
                .slackRecordId(dto.getSlackRecordId())
                .recipientId(dto.getRecipientId())
                .message(dto.getMessage())
                .createdAt(dto.getCreatedAt())
                .createdBy(dto.getCreatedBy())
                .build();
    }
}
