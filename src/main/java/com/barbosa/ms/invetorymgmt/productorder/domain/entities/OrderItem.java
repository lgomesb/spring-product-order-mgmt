package com.barbosa.ms.invetorymgmt.productorder.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "orderitem")
@Entity
public class OrderItem extends AbstractEntity {

    @Column()
    private UUID productId;
    @Column()
    private int quantity;

    @Builder
    public OrderItem(UUID productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}

