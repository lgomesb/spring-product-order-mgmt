package com.barbosa.ms.invetorymgmt.productorder.domain.dto;

import com.barbosa.ms.invetorymgmt.productorder.domain.records.in.ProductOrderRecordIn;
import lombok.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Data
public class ProductOrderResponseDTO extends ResponseDTO {

    private List<OrderItemDTO> items;

    private StatusProductOrderEnum status;

    public static ProductOrderResponseDTO create(ProductOrderRecordIn productorderRecordIn) {
        return ProductOrderResponseDTO.builder()
                .id(productorderRecordIn.id())
                .description(productorderRecordIn.description())
                .status(StatusProductOrderEnum.valueOf(productorderRecordIn.status()))
                .items(productorderRecordIn.items().stream().map(OrderItemDTO::create).toList())
                .build();
    }

    @Builder
    public ProductOrderResponseDTO(UUID id, String description, List<OrderItemDTO> items, StatusProductOrderEnum status) {
        super();
        super.setId(id);
        super.setDescription(description);
        this.items = items;
        this.status = status;
    }

    public List<OrderItemDTO> getItems() {
        items = items == null ? Collections.emptyList() : items;
        return items;
    }
}
