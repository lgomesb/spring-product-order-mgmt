package com.barbosa.ms.invetorymgmt.productorder.repositories.success;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Stream;

import com.barbosa.ms.invetorymgmt.productorder.domain.entities.OrderItem;
import com.barbosa.ms.invetorymgmt.productorder.repositories.OrderItemRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import com.barbosa.ms.invetorymgmt.productorder.ProductOrderApplicationTests;
import com.barbosa.ms.invetorymgmt.productorder.domain.entities.ProductOrder;
import com.barbosa.ms.invetorymgmt.productorder.repositories.ProductOrderRepository;

@ActiveProfiles(value = "test")
@DataJpaTest()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(classes = ProductOrderApplicationTests.class)
@TestInstance(Lifecycle.PER_CLASS)
class ProductOrderRepositorySuccessTest {

    @Autowired
    private ProductOrderRepository orderRepository;

    private static Stream<Arguments> provideProductOrderData() {
        return Stream.of(
          Arguments.of("A"),
          Arguments.of("I")
        );
    }

    
    @Test 
    @Order(0)
    void shouldSuccessfulInjectComponent() {
        assertNotNull(orderRepository);
    }

    @Order(1)
    @ParameterizedTest
    @MethodSource("provideProductOrderData")
    void shouldWhenCallCreate(String status) {

        ProductOrder productorder = ProductOrder
                .builder()
                .description("Test-01")
                .status(status)
                .build();
        Set<OrderItem> items = Collections.singleton(OrderItem
                .builder()
                .quantity(1)
                .productOrder(productorder)
                .productId(UUID.randomUUID())
                .build());

        productorder.setItems(items);
        orderRepository.saveAndFlush(productorder);

        assertNotNull(productorder, "Should return ProductOrder is not null");
        assertNotNull(productorder.getId());
        assertEquals(status, productorder.getStatus());
        System.out.println(productorder);

    }


    @Order(2)
    @ParameterizedTest
    @MethodSource("provideProductOrderData")
    void shouldWhenCallFindById(String status) {
        ProductOrder productorder = orderRepository.save(ProductOrder.builder().status(status).build());
        Optional<ProductOrder> oProductOrder = orderRepository.findById(productorder.getId());
        assertNotNull(oProductOrder.get(), "Should return ProductOrder is not null");
        assertNotNull(oProductOrder.get().getId(), "Should return ProductOrder ID is not null");
        assertNotNull(oProductOrder.get().getStatus(), "Should return ProductOrder NAME is not null");
    }

  
    @Order(3)
    @ParameterizedTest
    @MethodSource("provideProductOrderData")
    void shouldWhenCallUpdate(String status) {
        String statusUpdate = "I".equalsIgnoreCase(status)?"A":"I";
        ProductOrder productorder = orderRepository.save(ProductOrder.builder().status(status).build());
        Optional<ProductOrder> oProductOrder = orderRepository.findById(productorder.getId());
        ProductOrder newProductOrder = oProductOrder.get();
        newProductOrder.setStatus(statusUpdate);
        newProductOrder = orderRepository.save(newProductOrder);
        assertEquals(statusUpdate, newProductOrder.getStatus());
    }
  
    @Order(4)
    @ParameterizedTest
    @MethodSource("provideProductOrderData")
    void shouldWhenCallDelete(String status) {
        ProductOrder productorder = orderRepository.save(ProductOrder.builder().status(status).build());
        Optional<ProductOrder> oProductOrder = orderRepository.findById(productorder.getId());
        orderRepository.delete(oProductOrder.get());
        Optional<ProductOrder> findProductOrder = orderRepository.findById(oProductOrder.get().getId());
        assertFalse(findProductOrder.isPresent());
    }
}
