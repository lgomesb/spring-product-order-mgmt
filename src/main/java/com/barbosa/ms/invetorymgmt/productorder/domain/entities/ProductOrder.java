package com.barbosa.ms.invetorymgmt.productorder.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@Table(name = "productorder")
@Entity
public class ProductOrder extends AbstractEntity {
    
    public ProductOrder(String name) {
        super();
        super.setName(name);
    }
    
    @Builder()
    public ProductOrder(UUID id, String name) {
        super();
        super.setId(id);
        super.setName(name);
    }
}
