package com.delivery_signal.eureka.client.external.slack.application.service;

import com.delivery_signal.eureka.client.external.slack.application.dto.SlackRecordCreationDto;
import com.delivery_signal.eureka.client.external.slack.application.dto.request.CreateSlackRecordRequestDto;
import com.delivery_signal.eureka.client.external.slack.domain.model.SlackRecord;
import com.delivery_signal.eureka.client.external.slack.domain.repository.SlackRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlackRecordServiceV1 {

    private final SlackRecordRepository repository;

    /**
     * <p>SlackRecord 생성</p>
     * @param request 질문한유저 {@link CreateSlackRecordRequestDto}
     * @return {@link SlackRecordCreationDto}
     */
    public SlackRecordCreationDto createSlackRecord(CreateSlackRecordRequestDto request){
        SlackRecord slackRecord = SlackRecord.create(request);
        repository.save(slackRecord);
        return SlackRecordCreationDto.from(slackRecord);
    }
}
