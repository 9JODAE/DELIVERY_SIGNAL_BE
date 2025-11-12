package com.delivery_signal.eureka.client.user.infrastructure.repository;

import com.delivery_signal.eureka.client.user.domain.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserJpaRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findBySlackId(String slackId);
    List<User> findAllByUsernameContainingIgnoreCase(String keyword);
    List<User> findAllByOrganizationContainingIgnoreCase(String keyword);

}
