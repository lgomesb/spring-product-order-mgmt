package com.barbosa.ms.invetorymgmt.productorder.repositories.success;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.Optional;
import java.util.stream.Stream;

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
    private ProductOrderRepository repository;

    private static Stream<Arguments> provideProductOrderData() {        
        return Stream.of(
          Arguments.of("ProductOrder-Test-01"),
          Arguments.of("ProductOrder-Test-02")
        );
    }

    
    @Test 
    @Order(0)
    void shouldSuccessfulInjectComponent() {
        assertNotNull(repository);
    }

    @Order(1)
    @ParameterizedTest
    @MethodSource("provideProductOrderData")
    void shouldWhenCallCreate(String productorderName) {
        ProductOrder productorder = repository.saveAndFlush(new ProductOrder(productorderName));
        assertNotNull(productorder, "Should return ProductOrder is not null");
        assertNotNull(productorder.getId());
        assertEquals(productorderName, productorder.getName());        
    }


    @Order(2)
    @ParameterizedTest
    @MethodSource("provideProductOrderData")
    void shouldWhenCallFindById(String productorderName) {
        ProductOrder productorder = repository.save(new ProductOrder(productorderName));
        Optional<ProductOrder> oProductOrder = repository.findById(productorder.getId());
        assertNotNull(oProductOrder.get(), "Should return ProductOrder is not null");
        assertNotNull(oProductOrder.get().getId(), "Should return ProductOrder ID is not null");
        assertNotNull(oProductOrder.get().getName(), "Should return ProductOrder NAME is not null");
    }

  
    @Order(3)
    @ParameterizedTest
    @MethodSource("provideProductOrderData")
    void shouldWhenCallUpdate(String productorderName) {
        String productorderNameUpdate = "Test-Update-ProductOrder";
        ProductOrder productorder = repository.save(new ProductOrder(productorderName));
        Optional<ProductOrder> oProductOrder = repository.findById(productorder.getId());
        ProductOrder newProductOrder = oProductOrder.get();
        newProductOrder.setName(productorderNameUpdate);
        newProductOrder = repository.save(newProductOrder);
        assertEquals(productorderNameUpdate, newProductOrder.getName());
    }
  
    @Order(4)
    @ParameterizedTest
    @MethodSource("provideProductOrderData")
    void shouldWhenCallDelete(String productorderName) {
        ProductOrder productorder = repository.save(new ProductOrder(productorderName));
        Optional<ProductOrder> oProductOrder = repository.findById(productorder.getId());
        repository.delete(oProductOrder.get());
        Optional<ProductOrder> findProductOrder = repository.findById(oProductOrder.get().getId());
        assertFalse(findProductOrder.isPresent());
    }
}
