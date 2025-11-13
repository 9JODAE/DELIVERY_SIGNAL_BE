package com.delivery_signal.eureka.client.order.application.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDeliveryCommand {
    private UUID deliveryId;          // 주문에서 미리 생성한 UUID
    private UUID orderId;             // 주문 PK
    private UUID supplierCompanyId;   // 출발 업체 ID (공급자)
    private UUID receiverCompanyId;   // 도착 업체 ID (수령자)
    private UUID fromHubId;           // 출발 허브 ID (공급자 허브)
    private UUID toHubId;             // 도착 허브 ID (수령자 허브)
    private String address;           // 수령지 주소
}