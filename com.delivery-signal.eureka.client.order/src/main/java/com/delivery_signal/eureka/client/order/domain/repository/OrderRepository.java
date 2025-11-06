package com.delivery_signal.eureka.client.order.domain.repository;

import com.delivery_signal.eureka.client.order.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //자동주입이 되기 때문에 생략도 가능하지만 명시적으로 어노테이션을 붙였습니다.
public interface OrderRepository extends JpaRepository<Order, Integer>, OrderRepositoryCustom {

}
