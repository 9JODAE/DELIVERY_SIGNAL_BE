package com.delivery_signal.eureka.client.delivery.application.dto;

import com.delivery_signal.eureka.client.delivery.domain.model.Delivery;
import java.util.List;
import org.springframework.data.domain.Page;

public record PagedDeliveryResponse(
    List<DeliveryQueryResponse> deliveries,
    int totalPages,
    long totalElements,
    int currentPage,
    boolean isFirst,
    boolean isLast
) {
    public static PagedDeliveryResponse from(Page<Delivery> page, List<DeliveryQueryResponse> deliveryResponses) {
        return new PagedDeliveryResponse(
            deliveryResponses,
            page.getTotalPages(),
            page.getTotalElements(),
            page.getNumber(),
            page.isFirst(),
            page.isLast()
        );
    }
}
