package com.delivery_signal.eureka.client.company.domain.vo.company;

import com.delivery_signal.eureka.client.company.domain.entity.CompanyType;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CompanyInfo {
    private UUID companyId;
    private String companyName;
    private CompanyType type;
    private UUID hubId;
    private String address;
}



