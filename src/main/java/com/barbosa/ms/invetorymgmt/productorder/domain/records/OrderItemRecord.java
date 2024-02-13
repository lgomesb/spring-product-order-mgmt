package com.barbosa.ms.invetorymgmt.productorder.domain.records;

import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderItemRecord(UUID productId, int quantity) {

}
