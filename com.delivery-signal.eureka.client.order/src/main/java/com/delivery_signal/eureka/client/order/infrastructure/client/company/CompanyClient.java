package com.delivery_signal.eureka.client.order.infrastructure.client.company;

import com.delivery_signal.eureka.client.order.domain.vo.company.CompanyInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "company-service", url = "${internal.company.url}")
public interface CompanyClient {

    /**
     * 특정 업체 조회
     *
     * 반환할 info(dto 형식으로 주시면 됩니다.)
     * UUID companyId;
     * UUID hubId;
     * String address;
     *
     * @param companyId 조회할 업체 UUID
     * @return CompanyInfo
     */
    @GetMapping("/open-api/v1/companies/{companyId}")
    CompanyInfo getCompanyById(@PathVariable("companyId") UUID companyId);

}
