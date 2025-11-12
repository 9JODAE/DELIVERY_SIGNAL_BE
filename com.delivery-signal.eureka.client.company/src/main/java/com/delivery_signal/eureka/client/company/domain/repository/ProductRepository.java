package com.delivery_signal.eureka.client.company.domain.repository;

import com.delivery_signal.eureka.client.company.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository //명시적
public interface ProductRepository extends JpaRepository<Product, UUID>, ProductRepositoryCustom {
}
