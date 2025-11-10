package com.delivery_signal.eureka.client.order.infrastructure.client.company;

import com.delivery_signal.eureka.client.order.domain.vo.company.CompanyInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "company-service", url = "${internal.company.url}")
public interface CompanyClient {

    /**
     * 특정 업체의 존재검증을 api
     *
     * @param companyId 조회할 업체의 UUID
     * @return 같은 UUID 리턴 (존재하지 않을 시 업체 쪽에서 예외처리할 것으로 예상됨)
     */
    @GetMapping("open-api//companies/{companyId}")
    CompanyInfo getCompanyById(@PathVariable UUID companyId);
}
