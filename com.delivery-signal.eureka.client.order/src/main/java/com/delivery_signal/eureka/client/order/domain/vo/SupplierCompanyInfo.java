package com.delivery_signal.eureka.client.order.domain.vo;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

// 외부 API에서 받은 공급업체 정보
@Getter
@Builder
public class SupplierCompanyInfo {
    private final UUID id;
    private final String name;
    private final String location;

    public SupplierCompanyInfo(UUID id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }
}