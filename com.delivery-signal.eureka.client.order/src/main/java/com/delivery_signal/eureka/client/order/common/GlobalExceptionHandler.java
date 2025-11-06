package com.delivery_signal.eureka.client.order.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 404 - 요청한 리소스를 찾을 수 없을 때 발생
     * 예: 존재하지 않는 주문 ID로 조회 시
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiErrorResponse.of(e.getMessage(), LocalDateTime.now()));
    }

    /**
     * 400 - 유효하지 않은 상태에서의 요청
     * 예: 이미 완료된 건을 수정하려는 경우
     */
    @ExceptionHandler(InvalidStateException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidState(InvalidStateException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiErrorResponse.of(ex.getMessage(), LocalDateTime.now()));
    }
}


