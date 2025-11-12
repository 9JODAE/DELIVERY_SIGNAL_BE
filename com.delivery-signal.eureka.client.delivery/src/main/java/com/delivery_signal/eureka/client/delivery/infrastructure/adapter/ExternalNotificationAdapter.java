package com.delivery_signal.eureka.client.delivery.infrastructure.adapter;

import com.delivery_signal.eureka.client.delivery.application.port.NotificationPort;
import com.delivery_signal.eureka.client.delivery.application.service.ExternalServiceClient;
import com.delivery_signal.eureka.client.delivery.domain.vo.SlackMessageDetails;
import org.springframework.stereotype.Component;

/**
 * Adapter: External Service의 FeignClient를 사용해 NotificationPort를 구현합니다. (ACL 역할)
 */
@Component
public class ExternalNotificationAdapter implements NotificationPort {

    private final ExternalServiceClient externalServiceClient;

    public ExternalNotificationAdapter(ExternalServiceClient externalServiceClient) {
        this.externalServiceClient = externalServiceClient;
    }

    @Override
    public String sendSlackNotification(SlackMessageDetails messageDetails) {
        // Domain VO -> Infrastructure DTO 변환 (ACL 핵심 작업)
        ExternalServiceClient.CreateSlackMessageRequest requestDto =
            new ExternalServiceClient.CreateSlackMessageRequest(
                messageDetails.slackUserId(),
                messageDetails.orderNum(),
                messageDetails.orderUserInfo(),
                messageDetails.orderTime(),
                messageDetails.productInfo(),
                messageDetails.detailRequest(),
                messageDetails.origin(),
                messageDetails.layOver(),
                messageDetails.destination(),
                messageDetails.deliveryUserInfo()
            );

        try {
            // Feign client 호출
            ExternalServiceClient.ApiResponse<String> response =
                externalServiceClient.sendSlackMessage(requestDto);

            // 응답처리
            if (response.data() != null) {
                return response.data(); // 메시지 ID 또는 성공 메시지 반환
            } else {
                throw new RuntimeException("Slack 메시지 발송 실패: 응답 데이터 없음");
            }
        } catch (Exception e) {
            // 통신 실패나 외부 서비스 에러 발생 시 처리
            throw new RuntimeException("External Service 통신 오류: " + e.getMessage());
        }
    }
}
