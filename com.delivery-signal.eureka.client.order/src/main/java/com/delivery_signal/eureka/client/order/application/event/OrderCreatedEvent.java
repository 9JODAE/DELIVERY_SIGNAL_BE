package com.delivery_signal.eureka.client.order.application.event;


import com.delivery_signal.eureka.client.order.domain.entity.OrderProduct;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
public class OrderCreatedEvent {
    private final UUID orderId;
    private final UUID deliveryId;
    private final UUID supplier;  // 보내는 업체
    private final UUID receiver;  // 받는 업체
    private final List<OrderProduct> products;


    public OrderCreatedEvent(UUID orderId, UUID supplier, UUID receiver, List<OrderProduct> products, UUID deliveryId) {
        this.orderId = orderId;
        this.deliveryId = deliveryId;
        this.supplier = supplier;
        this.receiver = receiver;
        this.products = products;
    }

    @Getter
    public static class OrderProducts {
        private UUID productId;
        private int quantity;

        public OrderProducts(UUID productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
    }
}
