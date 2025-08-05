package com.barbosa.ms.invetorymgmt.productorder.repositories;

import com.barbosa.ms.invetorymgmt.productorder.domain.entities.ProductOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, UUID> {

    @Transactional(readOnly = true)
    Page<ProductOrder> findDistinctByDescriptionContaining(String description, PageRequest pageRequest);
}
