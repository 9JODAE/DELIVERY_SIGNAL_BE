package com.delivery_signal.eureka.client.order.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class OrderUpdateResponseDto {

    private UUID productId;
    private Long updatedBy;
    private LocalDateTime updatedAt;
    private String message;

    public static OrderUpdateResponseDto toResponse(UUID productId, Long updatedBy) {
        return OrderUpdateResponseDto.builder()
                .productId(productId)
                .updatedBy(updatedBy)
                .updatedAt(LocalDateTime.now())
                .message("주문 일부 정보가 수정되었습니다.")
                .build();
    }
}
