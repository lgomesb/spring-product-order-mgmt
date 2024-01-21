package com.barbosa.ms.productordermgmt.product-order-mgmt.services;

import java.util.List;
import java.util.UUID;

public interface DomainService<T> {
    T create(T recordObject);
    T findById(UUID id);
    void update(T recordObject);
    void delete(UUID id);
    List<T> listAll();

}
