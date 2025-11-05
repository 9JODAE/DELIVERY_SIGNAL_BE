package com.delivery_signal.eureka.client.external.slack.domain.model;


import com.delivery_signal.eureka.client.external.slack.application.dto.request.CreateSlackRecordRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "p_slack_records")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SlackRecord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String recipientId;

    @Column(nullable = false)
    private String messages;


    public static SlackRecord create(CreateSlackRecordRequestDto request){
        return SlackRecord.builder()
                .recipientId(request.getRecipientId())
                .messages(request.getMessage())
                .build();
    }

}
