package com.delivery_signal.eureka.client.delivery.application.service;

import com.delivery_signal.eureka.client.delivery.application.port.NotificationPort;
import com.delivery_signal.eureka.client.delivery.domain.entity.Delivery;
import com.delivery_signal.eureka.client.delivery.domain.entity.DeliveryRouteRecords;
import com.delivery_signal.eureka.client.delivery.domain.vo.SlackMessageDetails;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeliveryNotificationService {
    private final NotificationPort notificationPort;

    public DeliveryNotificationService(NotificationPort notificationPort) {
        this.notificationPort = notificationPort;
    }

    /**
     * 슬랙 메시지 발송 요청
     * @return 메시지 발송 결과 (메시지 ID 등)
     */
    public String requestSendSlackMessage(SlackMessageDetails details) {
        // Domain Port를 통해 외부 시스템에 요청을 전달합니다.
        // Adapter는 FeignClient를 통해 External Service의 AI/Slack API를 호출
        String result = notificationPort.sendSlackNotification(details);

        // 외부 서비스 통신 결과 반환
        log.info("Slack notification requested successfully. Result: {}", result);
        return result;
    }

    public SlackMessageDetails createSlackMessageDetails(Delivery delivery, List<DeliveryRouteRecords> records) {
        // 예시: 주문 정보, 담당자 정보(User Service, Logistics Infra Service 조회 필요), 경로 정보 등

        // TODO: DUMMY 수정 필요
        return new SlackMessageDetails(
//            delivery.getRecipientSlackId(), // 수신 ID
            "U09F0MQF3CL",
            delivery.getOrderId().toString(), // 주문 번호
            "홍길동 / msk@seafood.world", // orderUserInfo (Order Service 조회 필요)
            delivery.getCreatedAt().toString(),
            "쿠크다스 100박스", // productInfo (Product Service 조회 필요)
            "11월 20일 3시까지는 보내주세요!", // (Order Service 조회 필요)
            records.get(0).getDepartureHubName(), // 출발지
            getLayoverHubNames(records), // layOver (경유지 목록 조회 필요)
            delivery.getDeliveryAddress(), // 최종 도착지 주소
            "김길동" + delivery.getDeliveryManagerId().toString() // deliveryUserInfo (Logistics Infra Service 조회 필요)
        );
    }

    /**
     * DeliveryRouteRecords 리스트에서 경유지(중간 허브)의 이름만 추출하여 문자열로 결합합니다.
     * @param records 전체 경로 기록 리스트 (시퀀스 순)
     * @return 경유지 이름들을 쉼표로 연결한 문자열
     */
    private String getLayoverHubNames(List<DeliveryRouteRecords> records) {
        if (records == null || records.size() <= 2) {
            return "경유지 없음"; // 출발지 -> 목적지 직행
        }

        // 시퀀스 기준으로 정렬 (안정성 확보)
        records.sort(Comparator.comparing(DeliveryRouteRecords::getSequence));

        // 첫 번째(출발지)와 마지막(도착지) 기록을 제외한 중간 기록만 스트림 처리
        // 출발지 허브 ID 기준으로 경유지 추가
        // 첫 번째 (Sequence 0)와 마지막 요소를 건너뜀
        // 각 단계의 출발 허브 이름을 사용
        // 센터 단위로 결합
        return records.stream()
            // 첫 번째 (Sequence 0)와 마지막 요소를 건너뜀
            .skip(1)
            .limit(records.size() - 2)
            .map(DeliveryRouteRecords::getDepartureHubName) // 각 단계의 출발 허브 이름을 사용
            .collect(Collectors.joining(" 센터, ")) + " 센터";
    }
}

