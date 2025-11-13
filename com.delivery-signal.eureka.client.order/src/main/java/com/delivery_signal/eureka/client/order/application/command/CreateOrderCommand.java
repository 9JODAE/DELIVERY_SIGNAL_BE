package com.delivery_signal.eureka.client.order.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderCommand {
    private Long userId;
    private UUID supplierCompanyId;
    private UUID receiverCompanyId;
    private String recipient;          //주문자 이름
    private String recipientSlackId;   // 슬랙id
    private String requestNote;
    private List<OrderProductCommand> products;
}
