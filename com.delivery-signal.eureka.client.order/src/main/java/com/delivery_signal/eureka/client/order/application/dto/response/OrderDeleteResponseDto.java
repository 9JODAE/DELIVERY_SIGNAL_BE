package com.delivery_signal.eureka.client.order.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderDeleteResponseDto {
    private UUID orderId;
    private Long deletedBy;
    private LocalDateTime deletedAt;
    private String message;

    public static OrderDeleteResponseDto toResponse(UUID orderId, Long deletedBy, LocalDateTime deletedAt) {
        return OrderDeleteResponseDto.builder()
                .orderId(orderId)
                .deletedBy(deletedBy)
                .deletedAt(deletedAt)
                .message("주문이 삭제되었습니다.")
                .build();
    }
}
