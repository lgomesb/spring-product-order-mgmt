package com.barbosa.ms.invetorymgmt.productorder.services;

import com.barbosa.ms.invetorymgmt.productorder.domain.records.ProductOrderRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ProductOrderService extends DomainService<ProductOrderRecord>{

    void updateStatus(ProductOrderRecord productOrderRecord);

    Page<ProductOrderRecord> search(String name, PageRequest pageRequest);
}
