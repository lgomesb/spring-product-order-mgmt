package com.barbosa.ms.invetorymgmt.productorder.domain.dto;

import com.barbosa.ms.invetorymgmt.productorder.domain.records.ProductOrderRecord;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Data
public class ProductOrderResponseDTO extends ResponseDTO {

    private List<OrderItemDTO> items;

    private StatusProductOrderEnum status;

    public static ProductOrderResponseDTO create(ProductOrderRecord productorderRecord) {
        return ProductOrderResponseDTO.builder()
                .id(productorderRecord.id())
                .description(productorderRecord.description())
                .status(StatusProductOrderEnum.valueOf(productorderRecord.status()))
                .items(productorderRecord.items().stream().map(OrderItemDTO::create).toList())
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
