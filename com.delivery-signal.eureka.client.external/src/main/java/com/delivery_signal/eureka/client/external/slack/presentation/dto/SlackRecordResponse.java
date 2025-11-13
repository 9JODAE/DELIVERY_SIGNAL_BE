package com.delivery_signal.eureka.client.external.slack.presentation.dto;

import com.delivery_signal.eureka.client.external.slack.application.dto.SlackRecordDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class SlackRecordResponse {

    private final UUID slackRecordId;
    private final String recipientId;
    private final String message;
    private final LocalDateTime createdAt;
    private final Long createdBy;

    public static SlackRecordResponse from(
            SlackRecordDto slackRecordDto
    ){
        return SlackRecordResponse.builder()
                .slackRecordId(slackRecordDto.getSlackRecordId())
                .recipientId(slackRecordDto.getRecipientId())
                .message(slackRecordDto.getMessage())
                .createdAt(slackRecordDto.getCreatedAt())
                .createdBy(slackRecordDto.getCreatedBy())
                .build();
    }
}
