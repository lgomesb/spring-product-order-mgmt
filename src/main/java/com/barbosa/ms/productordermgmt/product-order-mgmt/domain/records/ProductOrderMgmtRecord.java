package com.barbosa.ms.productordermgmt.product-order-mgmt.domain.records;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ProductOrderMgmtRecord(UUID id, String name) {
    
}
