package com.delivery_signal.eureka.client.external.slack.application.dto;

import com.delivery_signal.eureka.client.external.slack.domain.model.SlackRecord;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class SlackRecordUpdateDto {

    private final UUID slackRecordId;
    private final String recipientId;
    private final String message;
    private final LocalDateTime updateAt;
    private final Long updatedBy;

    public static SlackRecordUpdateDto from(
            SlackRecord slackRecord
    ){
        return SlackRecordUpdateDto.builder()
                .slackRecordId(slackRecord.getId())
                .recipientId(slackRecord.getRecipientId())
                .message(slackRecord.getMessage())
                .updateAt(slackRecord.getUpdatedAt())
                .updatedBy(slackRecord.getUpdatedBy())
                .build();
    }
}
