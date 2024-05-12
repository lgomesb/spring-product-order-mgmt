package com.barbosa.ms.invetorymgmt.productorder.domain.dto;

import com.barbosa.ms.invetorymgmt.productorder.domain.records.OrderItemRecord;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class OrderItemDTO {

    private UUID productId;
    private int quantity;

    public static OrderItemDTO create(OrderItemRecord record) {
        return new OrderItemDTO(record.productId(), record.quantity());
    }
}
