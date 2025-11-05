package com.delivery_signal.eureka.client.external.slack.application.service;

import com.delivery_signal.eureka.client.external.slack.application.dto.SlackRecordCreationDto;
import com.delivery_signal.eureka.client.external.slack.application.dto.SlackRecordDto;
import com.delivery_signal.eureka.client.external.slack.application.dto.SlackRecordUpdateDto;
import com.delivery_signal.eureka.client.external.slack.application.dto.request.CreateSlackRecordRequestDto;
import com.delivery_signal.eureka.client.external.slack.application.dto.request.UpdateSlackRecordRequestDto;
import com.delivery_signal.eureka.client.external.slack.domain.model.SlackRecord;
import com.delivery_signal.eureka.client.external.slack.domain.repository.SlackRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SlackRecordServiceV1 {

    private final SlackRecordRepository repository;

    /**
     * <p>SlackRecord 생성</p>
     * @param requestDto 요청 Dto {@link CreateSlackRecordRequestDto}
     * @return {@link SlackRecordCreationDto}
     */
    public SlackRecordCreationDto createSlackRecord(CreateSlackRecordRequestDto requestDto){
        SlackRecord slackRecord = SlackRecord.create(requestDto);
        repository.save(slackRecord);
        return SlackRecordCreationDto.from(slackRecord);
    }


    public SlackRecordDto getSlackRecord(UUID slackRecordId){
        SlackRecord slackRecord = repository.findById(slackRecordId).orElseThrow(
                () -> new NoSuchElementException("Slack Record not found with ID: " + slackRecordId)
        );
        return SlackRecordDto.from(slackRecord);
    }

    @Transactional
    public SlackRecordUpdateDto updateSlackRecord(UUID slackRecordId, UpdateSlackRecordRequestDto recordRequestDto){
        SlackRecord slackRecord = repository.findById(slackRecordId).orElseThrow(
                () -> new NoSuchElementException("Slack Record not found with ID: " + slackRecordId)
        );
        slackRecord.update(recordRequestDto.getRecipientId(),recordRequestDto.getMessage());
        return SlackRecordUpdateDto.from(slackRecord);
    }
}
