package com.barbosa.ms.invetorymgmt.productorder.repositories.failed;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import com.barbosa.ms.invetorymgmt.productorder.ProductOrderApplicationTests;
import com.barbosa.ms.invetorymgmt.productorder.domain.entities.ProductOrder;
import com.barbosa.ms.invetorymgmt.productorder.repositories.ProductOrderRepository;

import jakarta.validation.ConstraintViolationException;

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
          Arguments.of("ProductOrder-Test-01"),
          Arguments.of("ProductOrder-Test-02")
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
        assertThrows(ConstraintViolationException.class, () -> {
            repository.saveAndFlush(new ProductOrder(null));
        }, "Should return Error when ProductOrder not null");
    }


    @Order(1)
    @ParameterizedTest
    @MethodSource("provideProductOrderData")
    void shouldFailWhenCallFindById(String productorderName) {
        repository.save(new ProductOrder(productorderName));
        Optional<ProductOrder> oProductOrder = repository.findById(UUID.randomUUID());
        assertThrows( ObjectNotFoundException.class, () -> {
            oProductOrder.orElseThrow(() ->
                 new ObjectNotFoundException("ProductOrder", UUID.randomUUID()));
        });
    }

  
    @Order(2)
    @ParameterizedTest
    @MethodSource("provideProductOrderData")
    void shouldFailWhenCallUpdate(String productorderName) {
        String productorderNameUpdate = "";
        ProductOrder productorder = repository.save(new ProductOrder(productorderName));
        Optional<ProductOrder> oProductOrder = repository.findById(productorder.getId());        
        assertThrows(ConstraintViolationException.class, () -> {
            ProductOrder newProductOrder = oProductOrder.get();
            newProductOrder.setName(productorderNameUpdate);
            repository.saveAndFlush(newProductOrder);
        }, "Should return Error when ProductOrder not blank or empty");
    }
  
    @Order(3)
    @ParameterizedTest
    @MethodSource("provideProductOrderData")
    void shouldFailWhenCallDelete(String productorderName) {
        ProductOrder productorder = new ProductOrder(UUID.randomUUID(), productorderName);
        Optional<ProductOrder> oProductOrder = repository.findById(productorder.getId());
        assertThrows( InvalidDataAccessApiUsageException.class, () -> {
            repository.delete(oProductOrder.orElse(null));
        }, "Should return Error when ProductOrder not blank or empty");
    }
}
