package com.barbosa.ms.productordermgmt.productordermgmt.services.impl;

import com.barbosa.ms.productordermgmt.productordermgmt.domain.entities.ProductOrderMgmt;
import com.barbosa.ms.productordermgmt.productordermgmt.domain.records.ProductOrderMgmtRecord;
import com.barbosa.ms.productordermgmt.productordermgmt.repositories.ProductOrderMgmtRepository;
import com.barbosa.ms.productordermgmt.productordermgmt.services.ProductOrderMgmtService;
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
        ProductOrderMgmt productordermgmt = repository.save(new ProductOrderMgmt(recordObject.name()) );
        return new ProductOrderMgmtRecord(productordermgmt.getId(), productordermgmt.getName());
    }

    @Override
    public ProductOrderMgmtRecord findById(UUID id) {
        ProductOrderMgmt productordermgmt = this.getProductOrderMgmtById(id);
        return new ProductOrderMgmtRecord(productordermgmt.getId(), productordermgmt.getName());
    }

    
    @Override
    public void update(ProductOrderMgmtRecord recordObject) {
        ProductOrderMgmt productordermgmt = this.getProductOrderMgmtById(recordObject.id());
        productordermgmt.setName(recordObject.name());
        productordermgmt.setModifieldOn(LocalDateTime.now());
        productordermgmt.setModifiedBy("99999");
        repository.save(productordermgmt);
    }
    
    @Override
    public void delete(UUID id) {
        ProductOrderMgmt productordermgmt = this.getProductOrderMgmtById(id);
        repository.delete(productordermgmt);

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
