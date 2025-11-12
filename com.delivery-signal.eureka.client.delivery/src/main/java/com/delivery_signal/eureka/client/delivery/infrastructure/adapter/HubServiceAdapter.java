package com.delivery_signal.eureka.client.delivery.infrastructure.adapter;

import com.delivery_signal.eureka.client.delivery.application.port.HubPort;
import com.delivery_signal.eureka.client.delivery.application.service.HubServiceClient;
import com.delivery_signal.eureka.client.delivery.application.service.HubServiceClient.ApiResponse;
import com.delivery_signal.eureka.client.delivery.application.service.HubServiceClient.PathResponse;
import com.delivery_signal.eureka.client.delivery.domain.vo.HubIdentifier;
import com.delivery_signal.eureka.client.delivery.domain.vo.HubRouteInfo;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class HubServiceAdapter implements HubPort {

    private final HubServiceClient hubServiceClient;

    public HubServiceAdapter(HubServiceClient hubServiceClient) {
        this.hubServiceClient = hubServiceClient;
    }

    @Override
    public boolean existsById(HubIdentifier hubIdentifier, Long userId, String role) {
        // Domain VO를 인프라 구현체(UUID)에 맞게 변환하여 FeignClient 호출
        return hubServiceClient.existsById(hubIdentifier.hubId(), userId, role);
    }

    @Override
    public List<HubRouteInfo> searchRoutes(HubIdentifier departureId, HubIdentifier arrivalId) {
        // FeignClient 호출
        ApiResponse<List<PathResponse>> responses = hubServiceClient.searchHubRoutes(
            departureId.hubId(), arrivalId.hubId());

        // 응답 유효성 검사
        if (responses == null || responses.data() == null) {
            throw new RuntimeException("허브 경로 조회 결과가 유효하지 않습니다.");
        }

        // 인프라 DTO -> 도메인 VO로 변환 (ACL 역할)
        return responses.data().stream()
            .map(pathResponse -> new HubRouteInfo(
                pathResponse.departureHubName(),
                pathResponse.arrivalHubName(),
                pathResponse.transitTime()
            ))
            .collect(Collectors.toList());
    }
}
