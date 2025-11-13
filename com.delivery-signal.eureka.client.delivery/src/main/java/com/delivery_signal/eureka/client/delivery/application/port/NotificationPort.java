package com.delivery_signal.eureka.client.delivery.application.port;

import com.delivery_signal.eureka.client.delivery.domain.vo.SlackMessageDetails;

/**
 * Port: Delivery Domain이 외부 알림/메시지 서비스와 통신하는 추상화 인터페이스 (Output Port)
 */
public interface NotificationPort {
    /**
     * 특정 사용자에게 Slack 메시지를 발송하도록 외부 서비스에 요청 (발송 메시지 상세 전달)
     */
    String sendSlackNotification(SlackMessageDetails messageDetails);
}
