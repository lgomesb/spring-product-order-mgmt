package com.barbosa.ms.productordermgmt.productordermgmt.domain.records;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ProductOrderMgmtRecord(UUID id, String name) {
    
}
