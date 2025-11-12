package com.delivery_signal.eureka.client.user.infrastructure.adapter;

import com.delivery_signal.eureka.client.user.domain.entity.User;
import com.delivery_signal.eureka.client.user.domain.repository.UserRepository;
import com.delivery_signal.eureka.client.user.infrastructure.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findBySlackId(String slackId) {
        return userJpaRepository.findBySlackId(slackId);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll();
    }


    @Override
    public List<User> findAllByUsernameContainingIgnoreCase(String keyword){
        return userJpaRepository.findAllByUsernameContainingIgnoreCase(keyword);
    }

    @Override
    public List<User> findAllByOrganizationContainingIgnoreCase(String keyword){
        return userJpaRepository.findAllByOrganizationContainingIgnoreCase(keyword);
    }




}
