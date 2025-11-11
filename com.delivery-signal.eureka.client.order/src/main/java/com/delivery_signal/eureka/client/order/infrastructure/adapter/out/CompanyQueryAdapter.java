package com.delivery_signal.eureka.client.order.infrastructure.adapter.out;

import com.delivery_signal.eureka.client.order.application.port.out.CompanyQueryPort;
import com.delivery_signal.eureka.client.order.common.NotFoundException;
import com.delivery_signal.eureka.client.order.domain.vo.company.CompanyInfo;
import com.delivery_signal.eureka.client.order.domain.vo.company.OrderCompanyInfo;
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

    // 주문 시 업체 정보 조회
    @Override
    public CompanyInfo getCompanyById(UUID companyId) {
        try {
            return companyClient.getCompanyById(companyId); // 그대로 반환
        } catch (FeignException.NotFound e) {
            throw new NotFoundException("업체", companyId);
        }
    }

    // 허브별 조회 시 최소 정보만 반환
    @Override
    public OrderCompanyInfo getCompanyByHubId(UUID hubId) {
        try {
            CompanyInfo info = companyClient.getCompanyByHubId(hubId);
            return toOrderCompanyInfo(info); // 최소 VO로 변환
        } catch (FeignException.NotFound e) {
            throw new NotFoundException("허브에 연결된 업체", hubId);
        }
    }

    // 최소 VO 변환
    private OrderCompanyInfo toOrderCompanyInfo(CompanyInfo info) {
        return OrderCompanyInfo.builder()
                .companyId(info.getCompanyId())
                .hubId(info.getHubId())
                .build();
    }
}
