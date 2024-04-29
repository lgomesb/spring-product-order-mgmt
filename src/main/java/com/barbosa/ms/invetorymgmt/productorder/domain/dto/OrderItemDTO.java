package com.barbosa.ms.invetorymgmt.productorder.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public class OrderItemDTO {

    private UUID productId;
    private int quantity;
}
