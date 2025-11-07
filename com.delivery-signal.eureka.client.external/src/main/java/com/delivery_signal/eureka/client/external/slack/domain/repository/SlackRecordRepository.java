package com.delivery_signal.eureka.client.external.slack.domain.repository;


import com.delivery_signal.eureka.client.external.slack.domain.model.SlackRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface SlackRecordRepository {

    void save(SlackRecord slackRecord);

    Optional<SlackRecord> findById(UUID slackRecordId);

    Page<SlackRecord> findAll(Pageable pageable);
}
