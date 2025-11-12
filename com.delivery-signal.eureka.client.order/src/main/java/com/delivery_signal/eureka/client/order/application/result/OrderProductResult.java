package com.delivery_signal.eureka.client.order.application.result;

import com.delivery_signal.eureka.client.order.domain.entity.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductResult {

    private UUID productId;
    private UUID orderId;
    private String productName;
    private BigDecimal productPriceAtOrder;
    private Integer quantity;

    // Entity -> DTO 변환용 정적 팩토리 메서드
    public static OrderProductResult from(OrderProduct orderProduct) {
        return new OrderProductResult(
                orderProduct.getProductId(),
                orderProduct.getOrder().getId(),
                orderProduct.getProductName(),
                orderProduct.getProductPriceAtOrder(),
                orderProduct.getTransferQuantity()
        );
    }
}

