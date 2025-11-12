package com.delivery_signal.eureka.client.company.application.port.out;

import com.delivery_signal.eureka.client.company.application.result.OrderCompanyResult;

import java.util.UUID;

/**
 * Order 서비스 통신용 Company 조회 Port
 */
public interface OrderCompanyQueryPort {
    OrderCompanyResult findCompanyById(UUID companyId);
}