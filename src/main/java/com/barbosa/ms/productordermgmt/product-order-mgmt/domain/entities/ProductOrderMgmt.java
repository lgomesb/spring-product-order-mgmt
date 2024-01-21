package com.barbosa.ms.productordermgmt.product-order-mgmt.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Table(name = "product-order-mgmt")
@Entity
public class ProductOrderMgmt extends AbstractEntity {
    
    public ProductOrderMgmt(String name) {
        super();
        super.setName(name);
    }
    
    @Builder()
    public ProductOrderMgmt(UUID id, String name) {
        super();
        super.setId(id);
        super.setName(name);
    }
}
