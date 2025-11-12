package com.delivery_signal.eureka.client.order.presentation.external.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 공통 API 응답 래퍼 클래스
 *
 * 모든 API 응답을 일관된 형식으로 제공
 * - 성공 응답: data 필드에 실제 데이터 포함
 * - 실패 응답: ErrorResponse 사용 (GlobalExceptionHandler에서 처리)
 *
 * @param <T> 응답 데이터 타입
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Builder
@AllArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private Long timestamp;

    /**
     * 성공 응답 생성 (데이터 포함)
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * 성공 응답 생성 (데이터 + 메시지)
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * 성공 응답 생성 (메시지만)
     */
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    /**
     * 실패 응답 생성 (에러 데이터)
     */
    public static <T> ApiResponse<T> error(T errorData) {
        return ApiResponse.<T>builder()
                .success(false)
                .data(errorData)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
