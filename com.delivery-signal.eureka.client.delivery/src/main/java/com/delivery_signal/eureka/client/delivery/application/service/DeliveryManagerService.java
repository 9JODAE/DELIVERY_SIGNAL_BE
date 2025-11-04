package com.delivery_signal.eureka.client.delivery.application.service;

import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryManager;
import com.delivery_signal.eureka.client.delivery.domain.repository.DeliveryManagerRepository;
import com.delivery_signal.eureka.client.delivery.presentation.dto.request.DeliveryManagerRegisterRequest;
import com.delivery_signal.eureka.client.delivery.presentation.dto.response.DeliveryManagerResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeliveryManagerService {

    private final DeliveryManagerRepository deliveryManagerRepository;
    // TODO: 추후 FeignClient 연동 예정
//    private final HubServiceClient hubServiceClient;
//    private final UserServiceClient userServiceClient;

    public DeliveryManagerService(DeliveryManagerRepository deliveryManagerRepository) {
        this.deliveryManagerRepository = deliveryManagerRepository;
    }

    @Transactional
    public DeliveryManagerResponse registerManager(Long userId, DeliveryManagerRegisterRequest request,
        String role) {
        // TODO: USER, HUB MSA 연동 & 유효성 검사 추가 예정

        // TODO: 새로운 배송 담당자가 추가되면 가장 마지막 순번으로 설정

        DeliveryManager manager = DeliveryManager.builder()
            .managerId(userId) // User ID를 Primary Key로 사용
            .hubId(request.hubId())
            .slackId(request.slackId())
            .managerType(request.type())
            .deliverySequence(0) // TODO: DUMMY 수정
            .build();

        DeliveryManager savedManager = deliveryManagerRepository.save(manager);
        return DeliveryManagerResponse.from(savedManager);
    }
}
