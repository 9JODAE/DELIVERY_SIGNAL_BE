package com.delivery_signal.eureka.client.order.infrastructure.external.company;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "company-service", url = "${external.company.url}")
public interface CompanyClient {

    @GetMapping("/companies/suppliers/{id}")
    UUID getSupplierCompany(@PathVariable UUID id);

    @GetMapping("/companies/receivers/{id}")
    UUID getReceiverCompany(@PathVariable UUID id);
}