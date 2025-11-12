package com.delivery_signal.eureka.client.user.domain.repository;

import com.delivery_signal.eureka.client.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;



public interface UserRepository{

    // User 저장
    User save(User user);

    // User 조회
    Optional<User> findById(Long userId);
    Optional<User> findByUsername(String username);
    Optional<User> findBySlackId(String slackId);
    List<User> findAll();

    // User 필터링

    List<User> findAllByUsernameContainingIgnoreCase(String keyword);
    List<User> findAllByOrganizationContainingIgnoreCase(String keyword);
}
