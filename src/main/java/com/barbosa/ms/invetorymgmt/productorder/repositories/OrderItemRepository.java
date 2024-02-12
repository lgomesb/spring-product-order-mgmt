package com.barbosa.ms.invetorymgmt.productorder.repositories;

import com.barbosa.ms.invetorymgmt.productorder.domain.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    
}
