package com.barbosa.ms.invetorymgmt.productorder.domain.dto;

import com.barbosa.ms.invetorymgmt.productorder.domain.records.ProductOrderRecord;
import lombok.Builder;

import java.util.UUID;

public class ProductOrderResponseDTO extends ResponseDTO {

    public static ProductOrderResponseDTO create(ProductOrderRecord productorderRecord) {
       return ProductOrderResponseDTO.builder()
                .id(productorderRecord.id())
                .name(productorderRecord.status())
                .build();
    }

    @Builder
    public ProductOrderResponseDTO(UUID id, String name) {
        super();
        super.setId(id);
        super.setName(name);
    }
}
