package com.delivery_signal.eureka.client.hub.infrastructure.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.delivery_signal.eureka.client.hub.domain.model.Hub;

public interface JpaHubRepository extends JpaRepository<Hub, UUID> {
}
