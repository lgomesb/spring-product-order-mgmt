package com.barbosa.ms.productordermgmt.productordermgmt.domain.dto;

import com.barbosa.ms.productordermgmt.productordermgmt.domain.records.ProductOrderMgmtRecord;
import lombok.Builder;

import java.util.UUID;

public class ProductOrderMgmtResponseDTO extends ResponseDTO {

    public static ProductOrderMgmtResponseDTO create(ProductOrderMgmtRecord productordermgmtRecord) {
       return ProductOrderMgmtResponseDTO.builder()
                .id(productordermgmtRecord.id())
                .name(productordermgmtRecord.name())
                .build();
    }

    @Builder
    public ProductOrderMgmtResponseDTO(UUID id, String name) {
        super();
        super.setId(id);
        super.setName(name);
    }
}
