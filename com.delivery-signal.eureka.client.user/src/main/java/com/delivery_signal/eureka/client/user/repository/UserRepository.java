package com.delivery_signal.eureka.client.user.repository;

import com.delivery_signal.eureka.client.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
