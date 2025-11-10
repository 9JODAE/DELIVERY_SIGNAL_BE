package com.delivery_signal.eureka.client.order.domain.vo.company;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

// 업체 정보
@Getter
@Builder
public class CompanyInfo {
    private final UUID companyId;
    private final UUID hubId;
    private final String address;

    public CompanyInfo(UUID companyId, UUID hubId, String address) {
        this.companyId = companyId;
        this.hubId = hubId;
        this.address = address;
    }
}

