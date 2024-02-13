package com.barbosa.ms.invetorymgmt.productorder.services.impl;

import com.barbosa.ms.invetorymgmt.productorder.domain.entities.OrderItem;
import com.barbosa.ms.invetorymgmt.productorder.domain.entities.ProductOrder;
import com.barbosa.ms.invetorymgmt.productorder.domain.records.OrderItemRecord;
import com.barbosa.ms.invetorymgmt.productorder.domain.records.ProductOrderRecord;
import com.barbosa.ms.invetorymgmt.productorder.repositories.ProductOrderRepository;
import com.barbosa.ms.invetorymgmt.productorder.services.ProductOrderService;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    private final ProductOrderRepository repository;

    @Autowired
    public ProductOrderServiceImpl(ProductOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductOrderRecord create(ProductOrderRecord recordObject) {

        ProductOrder productOrder = ProductOrder
                .builder()
                .status(recordObject.status())
                .build();

        final ProductOrder productOrderItem = productOrder;

        Set<OrderItem> items = recordObject.items()
                .stream()
                .map(i -> new OrderItem(i.productId(), i.quantity(), productOrderItem))
                .collect(Collectors.toSet());

        productOrder.setItems(items);
        productOrder = repository.save(productOrder);

        return ProductOrderRecord
                .builder()
                .status(productOrder.getStatus())
                .id(productOrder.getId())
                .items(productOrder.getItems()
                        .stream()
                        .map(i -> new OrderItemRecord(i.getProductId(), i.getQuantity()))
                        .collect(Collectors.toSet()))
                .build();
    }

    @Override
    public ProductOrderRecord findById(UUID id) {
        ProductOrder productOrder = this.getProductOrderById(id);
        return ProductOrderRecord
                .builder()
                .status(productOrder.getStatus())
                .id(productOrder.getId())
                .items(productOrder.getItems()
                        .stream()
                        .map(i -> new OrderItemRecord(i.getProductId(), i.getQuantity()))
                        .collect(Collectors.toSet()))
                .build();
    }

    
    @Override
    public void update(ProductOrderRecord recordObject) {
        ProductOrder productorder = this.getProductOrderById(recordObject.id());
        productorder.setStatus(recordObject.status());
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
                    .status(entity.getStatus())
                    .build())
            .toList();
    }
    
    
}
