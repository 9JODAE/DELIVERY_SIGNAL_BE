package com.delivery_signal.eureka.client.order.domain.vo.company;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class OrderCompanyInfo {
    private final UUID companyId;
    private final UUID hubId;
}
