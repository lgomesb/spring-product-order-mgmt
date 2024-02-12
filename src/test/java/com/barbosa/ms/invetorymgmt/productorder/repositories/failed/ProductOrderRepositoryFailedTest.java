package com.barbosa.ms.invetorymgmt.productorder.repositories.failed;

import com.barbosa.ms.invetorymgmt.productorder.ProductOrderApplicationTests;
import com.barbosa.ms.invetorymgmt.productorder.domain.entities.OrderItem;
import com.barbosa.ms.invetorymgmt.productorder.domain.entities.ProductOrder;
import com.barbosa.ms.invetorymgmt.productorder.repositories.ProductOrderRepository;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles(value = "test")
@DataJpaTest()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(classes = ProductOrderApplicationTests.class)
@TestInstance(Lifecycle.PER_CLASS)
class ProductOrderRepositoryFailedTest {

    @Autowired
    private ProductOrderRepository repository;


    private static Stream<Arguments> provideProductOrderData() {
        return Stream.of(
          Arguments.of("A"),
          Arguments.of("I")
        );
    }

    
    @BeforeAll
    void shouldSuccessfulInjectComponent() {
        assertNotNull(repository);
    }

    @Order(0)
    @Test()
    @DisplayName("Should return Exception when ProductOrder not null")
    void shouldFailWhenCallCreate() {
        ProductOrder order = ProductOrder.builder().status("").build();
        assertThrows(ConstraintViolationException.class, () -> {
            repository.saveAndFlush(order);
        }, "Should return Error when ProductOrder not null");
    }


    @Order(1)
    @ParameterizedTest
    @MethodSource("provideProductOrderData")
    void shouldFailWhenCallFindById(String status) {
        ProductOrder productOrder = ProductOrder
                .builder()
                .status(status)
                .items(Collections.singleton(OrderItem
                                .builder()
                                .productId(UUID.randomUUID())
                                .build()))
                .build();

        repository.save(productOrder);
        Optional<ProductOrder> oProductOrder = repository.findById(UUID.randomUUID());
        assertThrows( ObjectNotFoundException.class, () -> {
            oProductOrder.orElseThrow(() ->
                 new ObjectNotFoundException("ProductOrder", UUID.randomUUID()));
        });
    }

    @Order(2)
    @ParameterizedTest
    @MethodSource("provideProductOrderData")
    void shouldFailWhenCallUpdate(String status) {
        String statusUpdate = "";
        ProductOrder productorder = repository.save(ProductOrder.builder().status(status).build());
        Optional<ProductOrder> oProductOrder = repository.findById(productorder.getId());
        ProductOrder newProductOrder = oProductOrder.get();
        newProductOrder.setStatus(statusUpdate);

        assertThrows(ConstraintViolationException.class, () -> {
            repository.saveAndFlush(newProductOrder);
        });
    }
  
    @Order(3)
    @Test
    void shouldFailWhenCallDelete() {
        Optional<ProductOrder> oProductOrder = repository.findById(UUID.randomUUID());
        ProductOrder order = oProductOrder.orElse(null);

        assertThrows( InvalidDataAccessApiUsageException.class, () -> {
            repository.delete(order);
        }, "Should return Error when ProductOrder not blank or empty");
    }
}
