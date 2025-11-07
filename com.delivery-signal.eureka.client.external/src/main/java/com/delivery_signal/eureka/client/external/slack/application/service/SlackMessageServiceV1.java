package com.delivery_signal.eureka.client.external.slack.application.service;

import com.delivery_signal.eureka.client.external.slack.domain.service.SlackNotifier;
import com.delivery_signal.eureka.client.external.slack.infrastructure.slack.SlackApiAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SlackMessageServiceV1 {

    private final SlackNotifier slackNotifier;
    private final SlackApiAdapter apiAdapter;

    public String slackMessageSend(String targetSlackUserId, String message) {

//        String targetSlackUserId = apiAdapter.lookupUserIdByEmail(targetUserEmail);
//
//        if (targetSlackUserId == null) {
//            return "알림을 보낼 Slack 유저 ID를 찾을 수 없습니다: " + targetUserEmail;
//        }
        return slackNotifier.notifyUser(targetSlackUserId, message);
    }
}
