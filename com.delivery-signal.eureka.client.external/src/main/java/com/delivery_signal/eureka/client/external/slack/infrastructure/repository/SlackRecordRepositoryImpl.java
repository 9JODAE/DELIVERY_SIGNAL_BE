package com.delivery_signal.eureka.client.external.slack.infrastructure.repository;

import com.delivery_signal.eureka.client.external.slack.domain.model.SlackRecord;
import com.delivery_signal.eureka.client.external.slack.domain.repository.SlackRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class SlackRecordRepositoryImpl implements SlackRecordRepository {

    private final SlackRecordJpaRepository slackRecordJpaRepository;


    @Override
    public void save(SlackRecord slackRecord) {
        slackRecordJpaRepository.save(slackRecord);
    }

    @Override
    public Optional<SlackRecord> findById(UUID slackRecordId) {
        return slackRecordJpaRepository.findById(slackRecordId);
    }

    @Override
    public Page<SlackRecord> findAll(Pageable pageable) {
        return slackRecordJpaRepository.findAll(pageable);
    }
}
