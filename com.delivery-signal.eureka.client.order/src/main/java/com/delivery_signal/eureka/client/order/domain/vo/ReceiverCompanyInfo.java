package com.delivery_signal.eureka.client.order.domain.vo;

import lombok.Getter;

import java.util.UUID;

// 외부 API에서 받은 수령업체 정보
@Getter
public class ReceiverCompanyInfo {
    private final UUID id;
    private final String name;
    private final String location;

    public ReceiverCompanyInfo(UUID id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }
}

