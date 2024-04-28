package com.barbosa.ms.invetorymgmt.productorder.domain.dto;

import com.barbosa.ms.invetorymgmt.productorder.domain.records.in.ProductOrderRecordIn;
import lombok.Builder;

import java.util.UUID;

public class ProductOrderResponseDTO extends ResponseDTO {

    public static ProductOrderResponseDTO create(ProductOrderRecordIn productorderRecordIn) {
       return ProductOrderResponseDTO.builder()
                .id(productorderRecordIn.id())
                .description(productorderRecordIn.description())
                .build();
    }

    @Builder
    public ProductOrderResponseDTO(UUID id, String description) {
        super();
        super.setId(id);
        super.setDescription(description);
    }
}
