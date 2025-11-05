package com.delivery_signal.eureka.client.order.application.dto.response;

import com.delivery_signal.eureka.client.order.domain.entity.Order;
import com.delivery_signal.eureka.client.order.domain.entity.OrderProduct;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
public class OrderQueryResponseDto {
    private UUID productId;
    private String productName;
    private BigDecimal productPriceAtOrder;
    private Integer quantity;


    public OrderQueryResponseDto(UUID productId, String productName, BigDecimal productPriceAtOrder, Integer transferQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.productPriceAtOrder = productPriceAtOrder;
        this.quantity = transferQuantity;
    }

    public static OrderQueryResponseDto from(OrderProduct orderProduct) {
        return new OrderQueryResponseDto(
                orderProduct.getProductId(),
                orderProduct.getProductName(),
                orderProduct.getProductPriceAtOrder(),
                orderProduct.getTransferQuantity()
        );
    }
}
