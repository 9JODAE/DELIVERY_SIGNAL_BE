package com.delivery_signal.eureka.client.order.infrastructure.external.company;

import com.delivery_signal.eureka.client.order.application.service.external.CompanyService;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CompanyClientImpl implements CompanyService {

    private final CompanyClient companyClient;

    public CompanyClientImpl(CompanyClient companyClient) {
        this.companyClient = companyClient;
    }

    @Override
    public UUID getSupplierCompany(UUID supplierId) {
        return companyClient.getSupplierCompany(supplierId);
    }

    @Override
    public UUID getReceiverCompany(UUID receiverId) {
        return companyClient.getReceiverCompany(receiverId);
    }
}
