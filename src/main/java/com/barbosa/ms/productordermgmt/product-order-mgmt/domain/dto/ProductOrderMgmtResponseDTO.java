package com.barbosa.ms.productordermgmt.product-order-mgmt.domain.dto;

import com.barbosa.ms.productordermgmt.product-order-mgmt.domain.records.ProductOrderMgmtRecord;
import lombok.Builder;

import java.util.UUID;

public class ProductOrderMgmtResponseDTO extends ResponseDTO {

    public static ProductOrderMgmtResponseDTO create(ProductOrderMgmtRecord ProductOrderMgmtRecord) {
       return ProductOrderMgmtResponseDTO.builder()
                .id(ProductOrderMgmtRecord.id())
                .name(ProductOrderMgmtRecord.name())
                .build();
    }

    @Builder
    public ProductOrderMgmtResponseDTO(UUID id, String name) {
        super();
        super.setId(id);
        super.setName(name);
    }
}
