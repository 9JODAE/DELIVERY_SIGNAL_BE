package com.delivery_signal.eureka.client.external.slack.infrastructure.repository;

import com.delivery_signal.eureka.client.external.slack.domain.model.SlackRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
public interface SlackRecordJpaRepository extends JpaRepository<SlackRecord, UUID> {
}
