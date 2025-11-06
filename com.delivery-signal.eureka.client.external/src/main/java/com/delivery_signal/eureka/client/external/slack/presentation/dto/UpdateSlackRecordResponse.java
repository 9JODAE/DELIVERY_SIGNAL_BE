package com.delivery_signal.eureka.client.external.slack.presentation.dto;

import com.delivery_signal.eureka.client.external.slack.application.dto.SlackRecordUpdateDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class UpdateSlackRecordResponse {
    private final UUID slackRecordId;
    private final String recipientId;
    private final String message;
    private final LocalDateTime updatedAt;
    private final Long updatedBy;

    public static UpdateSlackRecordResponse from(
            SlackRecordUpdateDto updateDto
    ){
        return UpdateSlackRecordResponse.builder()
                .slackRecordId(updateDto.getSlackRecordId())
                .recipientId(updateDto.getRecipientId())
                .message(updateDto.getMessage())
                .updatedAt(updateDto.getUpdateAt())
                .updatedBy(updateDto.getUpdatedBy())
                .build();
    }
}
