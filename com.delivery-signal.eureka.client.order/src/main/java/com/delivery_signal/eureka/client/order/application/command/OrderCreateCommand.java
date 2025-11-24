package com.delivery_signal.eureka.client.order.application.command;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateCommand {
    private Long userId;
    private UUID supplierCompanyId;
    private UUID receiverCompanyId;
    private String recipient;          //주문자 이름
    private String recipientSlackId;   // 슬랙id
    private String requestNote;
    private List<OrderProductCommand> products;
}
