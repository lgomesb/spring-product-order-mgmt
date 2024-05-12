package com.barbosa.ms.invetorymgmt.productorder.domain.dto;

import com.barbosa.ms.invetorymgmt.productorder.domain.records.in.ProductOrderRecordIn;
import lombok.Builder;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Setter
public class ProductOrderResponseDTO extends ResponseDTO {

    private List<OrderItemDTO> items;

    public static ProductOrderResponseDTO create(ProductOrderRecordIn productorderRecordIn) {
       return ProductOrderResponseDTO.builder()
                .id(productorderRecordIn.id())
                .description(productorderRecordIn.description())
               .items(productorderRecordIn.items().stream()
                       .map(OrderItemDTO::create)
                       .toList())
                .build();
    }

    @Builder
    public ProductOrderResponseDTO(UUID id, String description, List<OrderItemDTO> items) {
        super();
        super.setId(id);
        super.setDescription(description);
        this.items = items;
    }

    public List<OrderItemDTO> getItems() {
        items = items == null ? Collections.emptyList() : items;
        return items;
    }
}
