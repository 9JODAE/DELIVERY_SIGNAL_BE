package com.delivery_signal.eureka.client.company.application.service;

import com.delivery_signal.eureka.client.company.application.port.out.OrderCompanyQueryPort;
import com.delivery_signal.eureka.client.company.application.result.OrderCompanyResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Order 서비스 통신용 Internal Company Service
 */
@Service
@RequiredArgsConstructor
public class InternalOrderCompanyService {

    private final OrderCompanyQueryPort orderCompanyQueryPort;

    public OrderCompanyResult getCompanyInfo(UUID companyId) {
        return orderCompanyQueryPort.findCompanyById(companyId);
    }
}
