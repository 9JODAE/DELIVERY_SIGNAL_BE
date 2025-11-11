package com.delivery_signal.eureka.client.user.domain.repository;

import com.delivery_signal.eureka.client.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByUsernameContainingIgnoreCase(String keyword);
    List<User> findAllByOrganizationContainingIgnoreCase(String keyword);
    Optional<User> findByUsername(String username);
    Optional<Object> findBySlackId(String slackId);
}
