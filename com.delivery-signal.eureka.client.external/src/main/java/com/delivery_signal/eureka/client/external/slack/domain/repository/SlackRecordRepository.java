package com.delivery_signal.eureka.client.external.slack.domain.repository;


import com.delivery_signal.eureka.client.external.slack.domain.model.SlackRecord;

import java.util.Optional;
import java.util.UUID;

public interface SlackRecordRepository {

    void save(SlackRecord slackRecord);

    Optional<SlackRecord> findById(UUID slackRecordId);
}
