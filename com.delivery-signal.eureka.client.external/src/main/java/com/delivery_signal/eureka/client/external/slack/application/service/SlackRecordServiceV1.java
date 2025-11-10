package com.delivery_signal.eureka.client.external.slack.application.service;

import com.delivery_signal.eureka.client.external.slack.application.dto.SlackRecordCreationDto;
import com.delivery_signal.eureka.client.external.slack.application.dto.SlackRecordDeleteDto;
import com.delivery_signal.eureka.client.external.slack.application.dto.SlackRecordDto;
import com.delivery_signal.eureka.client.external.slack.application.dto.SlackRecordUpdateDto;
import com.delivery_signal.eureka.client.external.slack.application.dto.request.UpdateSlackRecordRequestDto;
import com.delivery_signal.eureka.client.external.slack.domain.model.SlackRecord;
import com.delivery_signal.eureka.client.external.slack.domain.repository.SlackRecordRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
     * @param recipientId 수령인 식별자
     * @param message 메세지 내용
     * @return {@link SlackRecordCreationDto}
     */
    public SlackRecordCreationDto createSlackRecord(String recipientId, String message) {
        SlackRecord slackRecord = SlackRecord.create(recipientId, message);
        repository.save(slackRecord);
        return SlackRecordCreationDto.from(slackRecord);
    }

    /**
     * <p>특정 SlackRecord 조회</p>
     * @param slackRecordId 식별자
     * @return {@link SlackRecordDto}
     */
    public SlackRecordDto getSlackRecord(UUID slackRecordId) {
        SlackRecord slackRecord = getRecord(slackRecordId);
        return SlackRecordDto.from(slackRecord);
    }


    public Page<SlackRecordDto> getSlackRecordList(int page, int size, String sortBy, boolean isAsc){
        Pageable pageable = createPageable(page, size, sortBy, isAsc);
        Page<SlackRecord> slackRecordPage = repository.findAll(pageable);
        return slackRecordPage.map(SlackRecordDto::from);
    }

    /**
     * <p>특정 SlackRecord 수정</p>
     * @param slackRecordId 식별자
     * @param recordRequestDto {@link UpdateSlackRecordRequestDto}
     * @return {@link SlackRecordUpdateDto}
     */
    @Transactional
    public SlackRecordUpdateDto updateSlackRecord(UUID slackRecordId, UpdateSlackRecordRequestDto recordRequestDto) {
        SlackRecord slackRecord = getRecord(slackRecordId);
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
        SlackRecord slackRecord = getRecord(slackRecordId);
        slackRecord.softDelete();
        return SlackRecordDeleteDto.from(slackRecord);
    }


    private SlackRecord getRecord(UUID slackRecordId) {
        return repository.findById(slackRecordId).orElseThrow(
                () -> new NoSuchElementException("Slack Record not found with ID: " + slackRecordId)
        );
    }

    private Pageable createPageable(int page, int size, String sortBy, boolean isAsc) {

        int validatedSize = List.of(10, 30, 50).contains(size) ? size : 10;
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page - 1, validatedSize, Sort.by(direction, sortBy));
    }
}