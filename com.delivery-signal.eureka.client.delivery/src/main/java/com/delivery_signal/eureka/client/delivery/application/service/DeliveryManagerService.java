package com.delivery_signal.eureka.client.delivery.application.service;

import com.delivery_signal.eureka.client.delivery.domain.model.DeliveryManager;
import com.delivery_signal.eureka.client.delivery.domain.repository.DeliveryManagerRepository;
import com.delivery_signal.eureka.client.delivery.presentation.dto.request.DeliveryManagerRegisterRequest;
import com.delivery_signal.eureka.client.delivery.presentation.dto.response.DeliveryManagerResponse;
import java.util.NoSuchElementException;
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


    @Transactional(readOnly = true)
    public DeliveryManagerResponse getDeliveryManagerInfo(Long managerId, Long currUserId, String role) {
        // TODO: 권한 체크 -> 마스터 관리자가 아닌 경우, 본인의 ID와 요청 ID 비교
        // TODO: 배송 담당자는 본인의 정보만 조회 가능
        DeliveryManager manager = getDeliveryManagerByManagerId(managerId);
        return DeliveryManagerResponse.from(manager);
    }

    @Transactional
    public DeliveryManagerResponse updateManager(Long managerId, DeliveryManagerRegisterRequest request, Long currUserId, String role) {
        DeliveryManager manager = getDeliveryManagerByManagerId(managerId);
        // TODO: 허브 유효성 검사 추가

        // 순번은 여기서 변경하지 않음. 순번 재배열은 별도 로직
        manager.update(request.hubId(), request.slackId(), request.type());
        return DeliveryManagerResponse.from(manager);
    }

    private DeliveryManager getDeliveryManagerByManagerId(Long managerId) {
        return deliveryManagerRepository.findByManagerIdAndDeletedAtIsNull(managerId)
            .orElseThrow(() -> new NoSuchElementException("배송 담당자 정보를 찾을 수 없습니다"));
    }
}
