package com.delivery_signal.eureka.client.order.application.port.out;

import com.delivery_signal.eureka.client.order.domain.vo.company.CompanyInfo;
import com.delivery_signal.eureka.client.order.domain.vo.company.OrderCompanyInfo;

import java.util.UUID;

public interface CompanyQueryPort {

    //업체 Id 기준 업체, 허브, 주소 정보 조회
    CompanyInfo getCompanyById(UUID companyId);

    //업체 Id 기준 업체, 허브 조회
    OrderCompanyInfo getCompanyByHubId(UUID hubId);
}

