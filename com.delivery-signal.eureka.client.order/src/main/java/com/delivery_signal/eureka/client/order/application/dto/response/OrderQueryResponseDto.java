package com.delivery_signal.eureka.client.order.application.dto.response;

import com.delivery_signal.eureka.client.order.domain.entity.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderQueryResponseDto {
    private UUID productId;
    private String productName;
    private BigDecimal productPriceAtOrder;
    private Integer quantity;

    public static OrderQueryResponseDto from(OrderProduct orderProduct) {
        return new OrderQueryResponseDto(
                orderProduct.getProductId(),
                orderProduct.getProductName(),
                orderProduct.getProductPriceAtOrder(),
                orderProduct.getTransferQuantity()
        );
    }
}
