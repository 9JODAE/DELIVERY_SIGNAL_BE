package com.delivery_signal.eureka.client.order.application.service.external;

import java.util.UUID;

public interface CompanyService {
    UUID getSupplierCompany(UUID supplierId);
    UUID getReceiverCompany(UUID receiverId);
}
