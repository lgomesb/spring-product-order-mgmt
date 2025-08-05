package com.barbosa.ms.invetorymgmt.productorder.services.impl;

import com.barbosa.ms.invetorymgmt.productorder.domain.dto.StatusProductOrderEnum;
import com.barbosa.ms.invetorymgmt.productorder.domain.entities.OrderItem;
import com.barbosa.ms.invetorymgmt.productorder.domain.entities.ProductOrder;
import com.barbosa.ms.invetorymgmt.productorder.domain.records.OrderItemRecord;
import com.barbosa.ms.invetorymgmt.productorder.domain.records.ProductOrderRecord;
import com.barbosa.ms.invetorymgmt.productorder.exception.InvalidProductOrderStatusException;
import com.barbosa.ms.invetorymgmt.productorder.repositories.ProductOrderRepository;
import com.barbosa.ms.invetorymgmt.productorder.services.ProductOrderService;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.barbosa.ms.invetorymgmt.productorder.domain.dto.StatusProductOrderEnum.*;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    public static final String PRODUCT_ORDER_HAS_ALREADY_BEEN = "Product order has already been %s.";
    public static final String PRODUCT_ORDER_STATUS_MUST_BE_FIRST = "Product order status must be %s first";
    private final ProductOrderRepository repository;

    @Autowired
    public ProductOrderServiceImpl(ProductOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductOrderRecord create(ProductOrderRecord recordObject) {

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

        return ProductOrderRecord
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
    public ProductOrderRecord findById(UUID id) {
        ProductOrder productOrder = this.getProductOrderById(id);
        return ProductOrderRecord
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
    public void update(ProductOrderRecord recordObject) {
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
    public List<ProductOrderRecord> listAll() {
        return repository.findAll()
            .stream()
            .map(ProductOrderRecord::from)
            .toList();
    }


    @Override
    public void updateStatus(ProductOrderRecord productOrderRecord) {
        ProductOrder productorder = this.getProductOrderById(productOrderRecord.id());

        if(this.checkStatus(productorder, productOrderRecord)) {
            productorder.setStatus(productOrderRecord.status());
            repository.save(productorder);
        }
    }

    @Override
    public Page<ProductOrderRecord> search(String name, PageRequest pageRequest) {
        Page<ProductOrder> productOrders = repository.findDistinctByDescriptionContaining(name, pageRequest);
        return productOrders.map(ProductOrderRecord::from);
    }

    private boolean checkStatus(ProductOrder productorder, ProductOrderRecord productOrderRecord) {
        StatusProductOrderEnum asIsStatus = valueOf(productorder.getStatus());
        StatusProductOrderEnum toBeStatus = valueOf(productOrderRecord.status());

        if(COMPLETED.equals(asIsStatus) && !COMPLETED.equals(toBeStatus))
            throw new InvalidProductOrderStatusException(String.format(PRODUCT_ORDER_HAS_ALREADY_BEEN, asIsStatus.name()));

        if(toBeStatus.getValue() > asIsStatus.getValue()) {
            if (toBeStatus.getValue() - asIsStatus.getValue() > APPROVED.getValue()) {
                throw new InvalidProductOrderStatusException(String.format(PRODUCT_ORDER_STATUS_MUST_BE_FIRST, APPROVED.name()));
            } else if(asIsStatus.getValue() == REJECTED.getValue()) {
                throw new InvalidProductOrderStatusException(String.format(PRODUCT_ORDER_STATUS_MUST_BE_FIRST, DRAFT.name()));
            }
            return true;
        } else if (asIsStatus.getValue() == REJECTED.getValue())
            return true;
        else if (asIsStatus.getValue() > REJECTED.getValue()
                && toBeStatus.getValue() < asIsStatus.getValue()) {
            throw new InvalidProductOrderStatusException(String.format(PRODUCT_ORDER_HAS_ALREADY_BEEN, asIsStatus.name()));
        }

        return false;
    }
}
