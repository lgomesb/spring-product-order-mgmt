package com.barbosa.ms.invetorymgmt.productorder.domain.records;

import com.barbosa.ms.invetorymgmt.productorder.domain.entities.ProductOrder;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
public record ProductOrderRecord(UUID id,
                                 String description,
                                 String status,
                                 Set<OrderItemRecord> items) {

    public static ProductOrderRecord from(ProductOrder productOrder) {
        return ProductOrderRecord.builder()
                .id(productOrder.getId())
                .status(productOrder.getStatus())
                .description(productOrder.getDescription())
                .items(
                    productOrder.getItems()
                        .stream()
                        .map(OrderItemRecord::from)
                        .collect(Collectors.toSet())
                ).build();
    }

}
