package com.delivery_signal.eureka.client.delivery.presentation.mapper;

import com.delivery_signal.eureka.client.delivery.application.dto.ManagerQueryResponse;
import com.delivery_signal.eureka.client.delivery.presentation.dto.response.DeliveryManagerResponse;
import org.springframework.stereotype.Component;

/**
 * Application 계층의 DTO를 Presentation 계층의 ResponseDTO로 변환하는 역할
 */
@Component
public class DeliveryManagerMapper {
    /**
     * ManagerQueryResponse (Application DTO)를 DeliveryManagerResponse (Presentation DTO)로 변환
     */
    public DeliveryManagerResponse toResponse(ManagerQueryResponse queryResponse) {
        if (queryResponse == null) {
            return null;
        }

        return DeliveryManagerResponse.builder()
            .managerId(queryResponse.deliveryManagerId())
            .hubId(queryResponse.hubId() != null ? queryResponse.hubId() : null)
            .slackId(queryResponse.slackId())
            .managerType(queryResponse.managerType())
            .deliverySequence(queryResponse.deliverySequence())
            .createdAt(queryResponse.createdAt())
            .build();
    }
}
