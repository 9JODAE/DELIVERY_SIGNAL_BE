package com.delivery_signal.eureka.client.external.slack.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {


    //404 Not Found
    SLACK_RECORD_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 Slack Record 입니다."),

    //401 UNAUTHORIZED
    UNAUTHORIZED_SLACK_RECORD(HttpStatus.UNAUTHORIZED, "SlackRecord 관련권한이 없습니다."),

    //403 FORBIDDEN
    FORBIDDEN_SLACK_RECORD(HttpStatus.FORBIDDEN,"SlackRecord 관련권한이 없습니다."),

    //500 INTERNAL_SERVER_ERROR
    SLACK_NOTIFICATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Slack DM 전송에 실패했습니다. (Slack 응답 오류)"),
    SLACK_API_CALL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Slack API 호출 중 예측하지 못한 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String message;
}
