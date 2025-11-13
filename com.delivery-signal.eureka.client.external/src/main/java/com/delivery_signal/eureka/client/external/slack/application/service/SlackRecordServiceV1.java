package com.delivery_signal.eureka.client.external.slack.application.service;

import com.delivery_signal.eureka.client.external.slack.application.dto.SlackRecordCreationDto;
import com.delivery_signal.eureka.client.external.slack.application.dto.SlackRecordDeleteDto;
import com.delivery_signal.eureka.client.external.slack.application.dto.SlackRecordDto;
import com.delivery_signal.eureka.client.external.slack.application.dto.SlackRecordUpdateDto;
import com.delivery_signal.eureka.client.external.slack.application.dto.request.UpdateSlackRecordRequestDto;
import com.delivery_signal.eureka.client.external.slack.application.port.UserConnector;
import com.delivery_signal.eureka.client.external.slack.common.exception.ErrorCode;
import com.delivery_signal.eureka.client.external.slack.common.exception.GlobalException;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SlackRecordServiceV1 {

    private final SlackRecordRepository repository;
    private final UserConnector userConnector;
    private final UserValidationService userValidationService;
    @PersistenceContext
    private EntityManager em;

    /**
     * <p>SlackRecord 생성</p>
     * @param recipientId 수령인 식별자
     * @param message 메세지 내용
     * @param userId    권한 확인을 위한 사용자 ID
     * @return {@link SlackRecordCreationDto}
     */
    public SlackRecordCreationDto createSlackRecord(String recipientId, String message, Long userId) {
        userValidationService.validateSlackRecordAuthorization(userId);
        SlackRecord slackRecord = SlackRecord.create(recipientId, message);
        slackRecord.createdBy(userId);
        repository.save(slackRecord);
        return SlackRecordCreationDto.from(slackRecord);
    }

    /**
     * <p>특정 SlackRecord 조회</p>
     * @param slackRecordId 식별자
     * @param userId    권한 확인을 위한 사용자 ID
     * @return {@link SlackRecordDto}
     */
    public SlackRecordDto getSlackRecord(UUID slackRecordId, Long userId) {
        userValidationService.validateSlackRecordAuthorization(userId);
        SlackRecord slackRecord = getRecord(slackRecordId);
        String role = userConnector.getUserAuthorizationInfo(userId).getRole();
        System.out.println(role);
        return SlackRecordDto.from(slackRecord);
    }

    /**
     * <p>특정 SlackRecord 리스트 조회</p>
     * @param page      조회할 페이지 번호
     * @param size      한 페이지당 SlackRecord의 개수
     * @param sortBy    정렬 기준이 되는 필드 이름 (예: "createdAt")
     * @param isAsc     오름차순 정렬 여부 (true: 오름차순, false: 내림차순)
     * @param userId    권한 확인을 위한 사용자 ID
     * @return {@link SlackRecordDto}
     */
    public Page<SlackRecordDto> getSlackRecordList(int page, int size, String sortBy, boolean isAsc, Long userId){
        userValidationService.validateSlackRecordAuthorization(userId);
        Pageable pageable = createPageable(page, size, sortBy, isAsc);
        Page<SlackRecord> slackRecordPage = repository.findAll(pageable);
        return slackRecordPage.map(SlackRecordDto::from);
    }

    /**
     * <p>특정 SlackRecord 수정</p>
     * @param slackRecordId 식별자
     * @param recordRequestDto {@link UpdateSlackRecordRequestDto}
     * @param userId    권한 확인을 위한 사용자 ID
     * @return {@link SlackRecordUpdateDto}
     */
    @Transactional
    public SlackRecordUpdateDto updateSlackRecord(UUID slackRecordId, UpdateSlackRecordRequestDto recordRequestDto, Long userId) {
        SlackRecord slackRecord = getRecord(slackRecordId);
        userValidationService.validateSlackRecordAuthorization(userId);
        slackRecord.update(recordRequestDto.getRecipientId(), recordRequestDto.getMessage(), userId);
        em.flush();
        em.refresh(slackRecord);
        return SlackRecordUpdateDto.from(slackRecord);
    }

    /**
     * <p>특정 SlackRecord soft delete</p>
     * @param slackRecordId 식별자
     * @param userId    권한 확인을 위한 사용자 ID
     * @return {@link SlackRecordDeleteDto}
     */
    @Transactional
    public SlackRecordDeleteDto softDeleteSlackRecord(UUID slackRecordId, Long userId) {
        userValidationService.validateSlackRecordAuthorization(userId);
        SlackRecord slackRecord = getRecord(slackRecordId);
        slackRecord.softDelete(userId);
        return SlackRecordDeleteDto.from(slackRecord);
    }


    private SlackRecord getRecord(UUID slackRecordId) {
        return repository.findById(slackRecordId).orElseThrow(
                () -> new GlobalException(ErrorCode.SLACK_RECORD_NOT_FOUND)
        );
    }

    private Pageable createPageable(int page, int size, String sortBy, boolean isAsc) {
        int validatedSize = List.of(10, 30, 50).contains(size) ? size : 10;
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page - 1, validatedSize, Sort.by(direction, sortBy));
    }
}