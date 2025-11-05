package com.delivery_signal.eureka.client.external.slack.application.service;

import com.delivery_signal.eureka.client.external.slack.application.dto.SlackRecordCreationDto;
import com.delivery_signal.eureka.client.external.slack.application.dto.SlackRecordDeleteDto;
import com.delivery_signal.eureka.client.external.slack.application.dto.SlackRecordDto;
import com.delivery_signal.eureka.client.external.slack.application.dto.SlackRecordUpdateDto;
import com.delivery_signal.eureka.client.external.slack.application.dto.request.CreateSlackRecordRequestDto;
import com.delivery_signal.eureka.client.external.slack.application.dto.request.UpdateSlackRecordRequestDto;
import com.delivery_signal.eureka.client.external.slack.domain.model.SlackRecord;
import com.delivery_signal.eureka.client.external.slack.domain.repository.SlackRecordRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SlackRecordServiceV1 {

    private final SlackRecordRepository repository;
    @PersistenceContext
    private EntityManager em;

    /**
     * <p>SlackRecord 생성</p>
     * @param requestDto 요청 Dto {@link CreateSlackRecordRequestDto}
     * @return {@link SlackRecordCreationDto}
     */
    public SlackRecordCreationDto createSlackRecord(CreateSlackRecordRequestDto requestDto) {
        SlackRecord slackRecord = SlackRecord.create(requestDto.getRecipientId(), requestDto.getMessage());
        repository.save(slackRecord);
        return SlackRecordCreationDto.from(slackRecord);
    }

    /**
     * <p>특정 SlackRecord 조회</p>
     * @param slackRecordId 식별자
     * @return {@link SlackRecordDto}
     */
    public SlackRecordDto getSlackRecord(UUID slackRecordId) {
        SlackRecord slackRecord = repository.findById(slackRecordId).orElseThrow(
                () -> new NoSuchElementException("Slack Record not found with ID: " + slackRecordId)
        );
        return SlackRecordDto.from(slackRecord);
    }

    /**
     * <p>특정 SlackRecord 수정</p>
     * @param slackRecordId 식별자
     * @param recordRequestDto {@link UpdateSlackRecordRequestDto}
     * @return {@link SlackRecordUpdateDto}
     */
    @Transactional
    public SlackRecordUpdateDto updateSlackRecord(UUID slackRecordId, UpdateSlackRecordRequestDto recordRequestDto) {
        SlackRecord slackRecord = repository.findById(slackRecordId).orElseThrow(
                () -> new NoSuchElementException("Slack Record not found with ID: " + slackRecordId)
        );
        slackRecord.update(recordRequestDto.getRecipientId(), recordRequestDto.getMessage());
        em.flush();
        em.refresh(slackRecord);
        return SlackRecordUpdateDto.from(slackRecord);
    }

    /**
     * <p>특정 SlackRecord soft delete</p>
     * @param slackRecordId 식별자
     * @return {@link SlackRecordDeleteDto}
     */
    @Transactional
    public SlackRecordDeleteDto softDeleteSlackRecord(UUID slackRecordId) {
        SlackRecord slackRecord = repository.findById(slackRecordId).orElseThrow(
                () -> new NoSuchElementException("Slack Record not found with ID: " + slackRecordId)
        );
        slackRecord.softDelete();
        return SlackRecordDeleteDto.from(slackRecord);
    }
}