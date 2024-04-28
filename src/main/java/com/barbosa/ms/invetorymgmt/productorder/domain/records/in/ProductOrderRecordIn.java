package com.barbosa.ms.invetorymgmt.productorder.domain.records.in;

import com.barbosa.ms.invetorymgmt.productorder.domain.records.OrderItemRecord;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record ProductOrderRecordIn(UUID id, String description, String status, Set<OrderItemRecord> items) {

}
