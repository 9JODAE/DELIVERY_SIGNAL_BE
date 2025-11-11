package com.delivery_signal.eureka.client.company.domain.repository;

import com.delivery_signal.eureka.client.company.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllByDeletedAtIsNull(Pageable pageable);
    List<Product> findByHubIdAndDeletedAtIsNull(UUID hubId);
    void save();
}
