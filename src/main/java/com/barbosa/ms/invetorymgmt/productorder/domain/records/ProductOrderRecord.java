package com.barbosa.ms.invetorymgmt.productorder.domain.records;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ProductOrderRecord(UUID id, String name) {
    
}
