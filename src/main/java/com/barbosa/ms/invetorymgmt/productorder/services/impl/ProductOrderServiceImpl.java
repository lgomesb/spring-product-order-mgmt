package com.barbosa.ms.invetorymgmt.productorder.services.impl;

import com.barbosa.ms.invetorymgmt.productorder.domain.entities.ProductOrder;
import com.barbosa.ms.invetorymgmt.productorder.domain.records.ProductOrderRecord;
import com.barbosa.ms.invetorymgmt.productorder.repositories.ProductOrderRepository;
import com.barbosa.ms.invetorymgmt.productorder.services.ProductOrderService;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    @Autowired
    private ProductOrderRepository repository;

    @Override
    public ProductOrderRecord create(ProductOrderRecord recordObject) {
        ProductOrder productorder = repository.save(new ProductOrder(recordObject.name()) );
        return new ProductOrderRecord(productorder.getId(), productorder.getName());
    }

    @Override
    public ProductOrderRecord findById(UUID id) {
        ProductOrder productorder = this.getProductOrderById(id);
        return new ProductOrderRecord(productorder.getId(), productorder.getName());
    }

    
    @Override
    public void update(ProductOrderRecord recordObject) {
        ProductOrder productorder = this.getProductOrderById(recordObject.id());
        productorder.setName(recordObject.name());
        productorder.setModifiedOn(LocalDateTime.now());
        productorder.setModifiedBy("99999");
        repository.save(productorder);
    }
    
    @Override
    public void delete(UUID id) {
        ProductOrder productorder = this.getProductOrderById(id);
        repository.delete(productorder);

    }
    
    private ProductOrder getProductOrderById(UUID id) {
        return repository.findById(id)
                  .orElseThrow(()-> new ObjectNotFoundException("ProductOrder", id));
    }

    @Override
    public List<ProductOrderRecord> listAll() {
        return repository.findAll()
            .stream()
            .map(entity -> ProductOrderRecord.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .build())
            .toList();
    }
    
    
}
