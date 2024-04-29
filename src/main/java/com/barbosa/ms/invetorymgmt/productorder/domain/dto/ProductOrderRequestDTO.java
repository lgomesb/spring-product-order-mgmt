package com.barbosa.ms.invetorymgmt.productorder.domain.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(of = "items", callSuper = false)
@Data
public class ProductOrderRequestDTO extends DataDTO {

    private List<OrderItemDTO> items;

    public List<OrderItemDTO> getItems() {
        items = items == null ? Collections.emptyList() : items;
        return items;
    }
}
