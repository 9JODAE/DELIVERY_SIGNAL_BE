package com.delivery_signal.eureka.client.user.infrastructure.repository;

import com.delivery_signal.eureka.client.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByUsernameContainingIgnoreCase(String keyword);
    List<User> findAllByOrganizationContainingIgnoreCase(String keyword);
}
