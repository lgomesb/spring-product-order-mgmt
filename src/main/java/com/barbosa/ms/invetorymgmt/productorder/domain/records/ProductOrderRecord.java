package com.barbosa.ms.invetorymgmt.productorder.domain.records;

import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record ProductOrderRecord(UUID id, String status, Set<OrderItemRecord> items) {

}
