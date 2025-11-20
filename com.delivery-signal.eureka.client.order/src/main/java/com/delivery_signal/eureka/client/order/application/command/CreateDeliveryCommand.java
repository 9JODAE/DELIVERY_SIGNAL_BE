package com.delivery_signal.eureka.client.order.application.command;

import com.delivery_signal.eureka.client.order.domain.vo.user.UserRole;
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
    private Long userId;
    private String userRole;
    private UUID orderId;             // 주문 PK
    private UUID supplierCompanyId;   // 출발 업체 ID (공급자)
    private UUID receiverCompanyId;   // 도착 업체 ID (수령자)
    private UUID departureHubId;       // 출발 허브 ID (공급자 허브)
    private UUID destinationHubId;     // 도착 허브 ID (수령자 허브)
    private String address;           // 수령지 주소
    private String recipient;          //주문자 이름
    private String recipientSlackId;   // 슬랙id
}