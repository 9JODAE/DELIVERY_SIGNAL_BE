package com.delivery_signal.eureka.client.external.slack.application.dto;

import com.delivery_signal.eureka.client.external.slack.domain.model.SlackRecord;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class SlackRecordCreationDto {

    private final UUID slackRecordId;
    private final String recipientId;
    private final String message;
    private final LocalDateTime createdAt;
    private final Long createdBy;

    public static SlackRecordCreationDto from(
            SlackRecord slackRecord
    ){
        return SlackRecordCreationDto.builder()
                .slackRecordId(slackRecord.getId())
                .recipientId(slackRecord.getRecipientId())
                .message(slackRecord.getMessage())
                .createdAt(slackRecord.getCreatedAt())
                .createdBy(slackRecord.getCreatedBy())
                .build();
    }
}
