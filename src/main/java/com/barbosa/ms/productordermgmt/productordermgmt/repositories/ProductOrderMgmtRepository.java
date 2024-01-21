package com.barbosa.ms.productordermgmt.productordermgmt.repositories;

import com.barbosa.ms.productordermgmt.productordermgmt.domain.entities.ProductOrderMgmt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductOrderMgmtRepository extends JpaRepository<ProductOrderMgmt, UUID> {
    
}
