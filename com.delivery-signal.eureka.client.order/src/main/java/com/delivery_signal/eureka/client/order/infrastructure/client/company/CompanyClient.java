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
     * @param companyId 조회할 업체 UUID
     * @return CompanyInfo
     */
    @GetMapping("/open-api/companies/{companyId}")
    CompanyInfo getCompanyById(@PathVariable("companyId") UUID companyId);

    /**
     * 허브 ID 기준으로 소속 업체 조회
     *
     * @param hubId 조회할 허브 UUID
     * @return CompanyInfo
     */
    @GetMapping("/open-api/companies/hub/{hubId}")
    CompanyInfo getCompanyByHubId(@PathVariable("hubId") UUID hubId);
}
