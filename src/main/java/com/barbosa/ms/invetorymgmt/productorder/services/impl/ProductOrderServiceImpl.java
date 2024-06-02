package com.barbosa.ms.invetorymgmt.productorder.services.impl;

import com.barbosa.ms.invetorymgmt.productorder.domain.entities.OrderItem;
import com.barbosa.ms.invetorymgmt.productorder.domain.entities.ProductOrder;
import com.barbosa.ms.invetorymgmt.productorder.domain.records.OrderItemRecord;
import com.barbosa.ms.invetorymgmt.productorder.domain.records.in.ProductOrderRecordIn;
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
    public ProductOrderRecordIn create(ProductOrderRecordIn recordObject) {

        ProductOrder productOrder = ProductOrder
                .builder()
                .description(recordObject.description())
                .build();

        final ProductOrder productOrderItem = productOrder;

        Set<OrderItem> items = recordObject.items()
                .stream()
                .map(i -> new OrderItem(i.productId(), i.quantity(), productOrderItem))
                .collect(Collectors.toSet());

        productOrder.setItems(items);
        productOrder = repository.save(productOrder);

        return ProductOrderRecordIn
                .builder()
                .status(productOrder.getStatus())
                .description(productOrder.getDescription())
                .id(productOrder.getId())
                .items(productOrder.getItems()
                        .stream()
                        .map(i -> new OrderItemRecord(i.getProductId(), i.getQuantity()))
                        .collect(Collectors.toSet()))
                .build();
    }

    @Override
    public ProductOrderRecordIn findById(UUID id) {
        ProductOrder productOrder = this.getProductOrderById(id);
        return ProductOrderRecordIn
                .builder()
                .description(productOrder.getDescription())
                .status(productOrder.getStatus())
                .id(productOrder.getId())
                .items(productOrder.getItems()
                        .stream()
                        .map(i -> new OrderItemRecord(i.getProductId(), i.getQuantity()))
                        .collect(Collectors.toSet()))
                .build();
    }

    @Override
    public void update(ProductOrderRecordIn recordObject) {
        ProductOrder productOrder = this.getProductOrderById(recordObject.id());
        productOrder.setDescription(recordObject.description());
        productOrder.setStatus(recordObject.description());
        productOrder.setModifiedOn(LocalDateTime.now());
        productOrder.setModifiedBy("99999");
        repository.save(productOrder);
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
    public List<ProductOrderRecordIn> listAll() {
        return repository.findAll()
            .stream()
            .map(entity -> ProductOrderRecordIn.builder()
                    .id(entity.getId())
                    .status(entity.getStatus())
                    .description(entity.getDescription())
                    .items(entity.getItems()
                            .stream()
                            .map(i -> new OrderItemRecord(i.getProductId(), i.getQuantity()))
                                .collect(Collectors.toSet()))
                    .build())
            .toList();
    }
    
    
}
