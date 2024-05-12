package com.barbosa.ms.invetorymgmt.productorder.domain.dto;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@EqualsAndHashCode(of = "items", callSuper = false)
@Data
public class ProductOrderRequestDTO extends DataDTO {

    private List<OrderItemDTO> items;

    public List<OrderItemDTO> getItems() {
        items = items == null ? Collections.emptyList() : items;
        return items;
    }

    @Builder
    public ProductOrderRequestDTO(String description, List<OrderItemDTO> items) {
        super(description);
        this.items = items;
    }
}
