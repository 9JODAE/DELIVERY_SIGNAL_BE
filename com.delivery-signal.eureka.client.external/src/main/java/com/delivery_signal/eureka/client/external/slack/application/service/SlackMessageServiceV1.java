package com.delivery_signal.eureka.client.external.slack.application.service;

import com.delivery_signal.eureka.client.external.ai.application.GeminiAiCallable;
import com.delivery_signal.eureka.client.external.slack.application.dto.request.CreateSlackMessageRequestDto;
import com.delivery_signal.eureka.client.external.slack.domain.service.SlackNotifier;
import com.delivery_signal.eureka.client.external.slack.infrastructure.slack.SlackApiAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlackMessageServiceV1 {

    private final SlackNotifier slackNotifier;
    private final GeminiAiCallable geminiAiCallable;
    private final SlackRecordServiceV1 slackRecordServiceV1;

    public String slackMessageSend(String targetSlackUserId, String message) {

        return slackNotifier.notifyUser(targetSlackUserId, message);
    }

    public String notificationMessageSend(CreateSlackMessageRequestDto requestDto){
        String prompt = createPromptFromDto(requestDto);
        String response;
        try {
            response = geminiAiCallable.getResponse(prompt);
            slackRecordServiceV1.createSlackRecord(requestDto.getSlackUserId(),response);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            response = "error";
        }
        return slackNotifier.notifyUser(requestDto.getSlackUserId(), response);
    }

    public String createPromptFromDto(CreateSlackMessageRequestDto requestDto){
        String info =
                "주문 번호 : " +requestDto.getOrderNum()+
                        "\n주문자 정보 : " +requestDto.getOrderUserInfo()+
                        "\n주문 시간 : " +requestDto.getOrderTime()+
                        "\n상품 정보 : " +requestDto.getProductInfo()+
                        "\n요청 사항 : " +requestDto.getDetailRequest()+
                        "\n발송지 : " +requestDto.getOrigin()+
                        "\n경유지 : " +requestDto.getLayOver()+
                        "\n도착지 : " +requestDto.getDestination()+
                        "\n배송 담당자 : " +requestDto.getDeliveryUserInfo();
        return info+"\n 해당정보를 고려해서 납기에 맞출수있는 최종 발송 시한을 알려줘 답변은 '최종 발송시한은 ** 입니다.'형태로 보내줘";
    }
}
