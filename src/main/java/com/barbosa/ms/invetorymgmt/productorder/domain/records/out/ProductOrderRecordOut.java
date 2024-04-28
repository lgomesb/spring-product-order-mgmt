package com.barbosa.ms.invetorymgmt.productorder.domain.records.out;

import com.barbosa.ms.invetorymgmt.productorder.domain.records.OrderItemRecord;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record ProductOrderRecordOut(UUID id, String status, Set<OrderItemRecord> items) {

}
