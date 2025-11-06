package com.delivery_signal.eureka.client.external.slack.presentation.dto;

import com.delivery_signal.eureka.client.external.slack.application.dto.SlackRecordDeleteDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class DeleteSlackRecordResponse {

    private final UUID slackRecordId;
    private final LocalDateTime deletedAt;
    private final Long deletedBy;

    public static DeleteSlackRecordResponse from(
            SlackRecordDeleteDto dto
    ){
        return DeleteSlackRecordResponse.builder()
                .slackRecordId(dto.getSlackRecordId())
                .deletedAt(dto.getDeletedAt())
                .deletedBy(dto.getDeletedBy())
                .build();
    }
}
