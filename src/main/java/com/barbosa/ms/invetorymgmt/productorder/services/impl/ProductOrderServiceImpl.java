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
import java.util.*;
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

        recordObject.items().forEach(i -> {
            Optional<OrderItem> oOrderItem = productOrder.getItems().stream()
                    .filter(item -> item.getProductId().equals(i.productId()))
                    .findFirst();

            if(oOrderItem.isPresent()) {
                oOrderItem.get().setQuantity(i.quantity());
            } else {
                productOrder.addItem(
                        OrderItem.builder()
                                .productId(i.productId())
                                .quantity(i.quantity())
                                .build()
                );
            }

        });

        Set<OrderItem> orderItemsRemove = new HashSet<>();
        for (OrderItem item : productOrder.getItems()) {
            if (recordObject.items().stream().noneMatch(i -> item.getProductId().equals(i.productId()))){
                orderItemsRemove.add(item);
            }
        }
        orderItemsRemove.forEach(productOrder::removeItem);
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


    @Override
    public void updateStatus(ProductOrderRecordIn productOrderRecordIn) {
        ProductOrder productorder = this.getProductOrderById(productOrderRecordIn.id());
        productorder.setStatus(productOrderRecordIn.status());
        repository.save(productorder);
    }
}
