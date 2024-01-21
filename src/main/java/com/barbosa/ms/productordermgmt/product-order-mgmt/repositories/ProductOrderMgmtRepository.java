package com.barbosa.ms.productordermgmt.product-order-mgmt.repositories;

import com.barbosa.ms.productordermgmt.product-order-mgmt.domain.entities.ProductOrderMgmt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductOrderMgmtRepository extends JpaRepository<ProductOrderMgmt, UUID> {
    
}
