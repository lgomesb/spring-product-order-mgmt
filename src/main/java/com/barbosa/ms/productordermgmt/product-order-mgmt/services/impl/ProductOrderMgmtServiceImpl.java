package com.barbosa.ms.productordermgmt.product-order-mgmt.services.impl;

import com.barbosa.ms.productordermgmt.product-order-mgmt.domain.entities.ProductOrderMgmt;
import com.barbosa.ms.productordermgmt.product-order-mgmt.domain.records.ProductOrderMgmtRecord;
import com.barbosa.ms.productordermgmt.product-order-mgmt.repositories.ProductOrderMgmtRepository;
import com.barbosa.ms.productordermgmt.product-order-mgmt.services.ProductOrderMgmtService;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProductOrderMgmtServiceImpl implements ProductOrderMgmtService {

    @Autowired
    private ProductOrderMgmtRepository repository;

    @Override
    public ProductOrderMgmtRecord create(ProductOrderMgmtRecord recordObject) {
        ProductOrderMgmt ProductOrderMgmt = repository.save(new ProductOrderMgmt(recordObject.name()) );
        return new ProductOrderMgmtRecord(ProductOrderMgmt.getId(), ProductOrderMgmt.getName());
    }

    @Override
    public ProductOrderMgmtRecord findById(UUID id) {
        ProductOrderMgmt ProductOrderMgmt = this.getProductOrderMgmtById(id);
        return new ProductOrderMgmtRecord(ProductOrderMgmt.getId(), ProductOrderMgmt.getName());
    }

    
    @Override
    public void update(ProductOrderMgmtRecord recordObject) {
        ProductOrderMgmt ProductOrderMgmt = this.getProductOrderMgmtById(recordObject.id());
        ProductOrderMgmt.setName(recordObject.name());
        ProductOrderMgmt.setModifieldOn(LocalDateTime.now());
        ProductOrderMgmt.setModifiedBy("99999");
        repository.save(ProductOrderMgmt);      
    }
    
    @Override
    public void delete(UUID id) {
        ProductOrderMgmt ProductOrderMgmt = this.getProductOrderMgmtById(id);
        repository.delete(ProductOrderMgmt);

    }
    
    private ProductOrderMgmt getProductOrderMgmtById(UUID id) {
        return repository.findById(id)
                  .orElseThrow(()-> new ObjectNotFoundException("ProductOrderMgmt", id));
    }

    @Override
    public List<ProductOrderMgmtRecord> listAll() {
        return repository.findAll()
            .stream()
            .map(entity -> ProductOrderMgmtRecord.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .build())
            .toList();
    }
    
    
}
