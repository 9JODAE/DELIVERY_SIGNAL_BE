package com.delivery_signal.eureka.client.external.slack.application.dto;

import com.delivery_signal.eureka.client.external.slack.domain.model.SlackRecord;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class SlackRecordDeleteDto {

    private final UUID slackRecordId;
    private final LocalDateTime deletedAt;
    private final Long deletedBy;

    public static SlackRecordDeleteDto from(
            SlackRecord slackRecord
    ){
        return SlackRecordDeleteDto.builder()
                .slackRecordId(slackRecord.getId())
                .deletedAt(slackRecord.getDeletedAt())
                .deletedBy(slackRecord.getDeletedBy())
                .build();
    }
}
