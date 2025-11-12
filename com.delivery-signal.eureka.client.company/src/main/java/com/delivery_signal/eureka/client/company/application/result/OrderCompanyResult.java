package com.delivery_signal.eureka.client.company.application.result;


import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * Order 서비스 통신용 업체 정보 Result
 *
 * Order Service에서 내부 API 호출 시 반환되는 업체 정보
 */
@Getter
@Builder
public class OrderCompanyResult {
    private final UUID companyId;
    private final UUID hubId;
    private final String address;
}


