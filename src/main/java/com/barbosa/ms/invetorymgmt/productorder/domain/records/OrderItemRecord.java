package com.barbosa.ms.invetorymgmt.productorder.domain.records;

import com.barbosa.ms.invetorymgmt.productorder.domain.entities.OrderItem;
import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderItemRecord(UUID productId, int quantity) {

    public static OrderItemRecord from(OrderItem item) {
        return new OrderItemRecord(item.getProductId(), item.getQuantity());
    }
}
