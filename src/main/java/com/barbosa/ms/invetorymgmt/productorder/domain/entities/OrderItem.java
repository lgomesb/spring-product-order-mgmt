package com.barbosa.ms.invetorymgmt.productorder.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString(of = {"productId", "quantity"})
@NoArgsConstructor
@Table(name = "orderitem")
@Entity
public class OrderItem extends AbstractEntity {

    @Column()
    private UUID productId;

    @Column()
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "productOrder_id", nullable = false)
    private ProductOrder productOrder;
    @Builder
    public OrderItem(UUID productId, int quantity, ProductOrder productOrder) {
        this.productId = productId;
        this.quantity = quantity;
        this.productOrder = productOrder;
    }
}

