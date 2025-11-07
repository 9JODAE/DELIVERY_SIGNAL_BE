package com.delivery_signal.eureka.client.order.infrastructure.adapter.out;

import com.delivery_signal.eureka.client.order.application.port.out.CompanyQueryPort;
import com.delivery_signal.eureka.client.order.common.NotFoundException;
import com.delivery_signal.eureka.client.order.domain.vo.company.CompanyInfo;
import com.delivery_signal.eureka.client.order.infrastructure.client.company.CompanyClient;
import feign.FeignException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CompanyQueryAdapter implements CompanyQueryPort {
    private final CompanyClient companyClient;

    public CompanyQueryAdapter(CompanyClient companyClient) {
        this.companyClient = companyClient;
    }

    @Override
    public CompanyInfo getCompanyById(UUID companyId) {
        try {
            // Feign이 이미 CompanyInfo로 역직렬화
            return companyClient.getCompanyById(companyId);
        } catch (FeignException.NotFound e) {
            // Infra 예외 → Application 친화적 예외 변환
            throw new NotFoundException("업체", companyId);
        }
    }

}
