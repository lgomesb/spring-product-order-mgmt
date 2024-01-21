package com.barbosa.ms.productordermgmt.productordermgmt.repositories.success;

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
import com.barbosa.ms.productordermgmt.productordermgmt.ProductOrderMgmtApplicationTests;
import com.barbosa.ms.productordermgmt.productordermgmt.domain.entities.ProductOrderMgmt;
import com.barbosa.ms.productordermgmt.productordermgmt.repositories.ProductOrderMgmtRepository;

@ActiveProfiles(value = "test")
@DataJpaTest()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(classes = ProductOrderMgmtApplicationTests.class)
@TestInstance(Lifecycle.PER_CLASS)
class ProductOrderMgmtRepositorySuccessTest {

    @Autowired
    private ProductOrderMgmtRepository repository;

    private static Stream<Arguments> provideProductOrderMgmtData() {        
        return Stream.of(
          Arguments.of("ProductOrderMgmt-Test-01"),
          Arguments.of("ProductOrderMgmt-Test-02")
        );
    }

    
    @Test 
    @Order(0)
    void shouldSuccessfulInjectComponent() {
        assertNotNull(repository);
    }

    @Order(1)
    @ParameterizedTest
    @MethodSource("provideProductOrderMgmtData")
    void shouldWhenCallCreate(String productordermgmtName) {
        ProductOrderMgmt productordermgmt = repository.saveAndFlush(new ProductOrderMgmt(productordermgmtName));
        assertNotNull(productordermgmt, "Should return ProductOrderMgmt is not null");
        assertNotNull(productordermgmt.getId());
        assertEquals(productordermgmtName, productordermgmt.getName());        
    }


    @Order(2)
    @ParameterizedTest
    @MethodSource("provideProductOrderMgmtData")
    void shouldWhenCallFindById(String productordermgmtName) {
        ProductOrderMgmt productordermgmt = repository.save(new ProductOrderMgmt(productordermgmtName));
        Optional<ProductOrderMgmt> oProductOrderMgmt = repository.findById(productordermgmt.getId());
        assertNotNull(oProductOrderMgmt.get(), "Should return ProductOrderMgmt is not null");
        assertNotNull(oProductOrderMgmt.get().getId(), "Should return ProductOrderMgmt ID is not null");
        assertNotNull(oProductOrderMgmt.get().getName(), "Should return ProductOrderMgmt NAME is not null");
    }

  
    @Order(3)
    @ParameterizedTest
    @MethodSource("provideProductOrderMgmtData")
    void shouldWhenCallUpdate(String productordermgmtName) {
        String productordermgmtNameUpdate = "Test-Update-ProductOrderMgmt";
        ProductOrderMgmt productordermgmt = repository.save(new ProductOrderMgmt(productordermgmtName));
        Optional<ProductOrderMgmt> oProductOrderMgmt = repository.findById(productordermgmt.getId());
        ProductOrderMgmt newProductOrderMgmt = oProductOrderMgmt.get();
        newProductOrderMgmt.setName(productordermgmtNameUpdate);
        newProductOrderMgmt = repository.save(newProductOrderMgmt);
        assertEquals(productordermgmtNameUpdate, newProductOrderMgmt.getName());
    }
  
    @Order(4)
    @ParameterizedTest
    @MethodSource("provideProductOrderMgmtData")
    void shouldWhenCallDelete(String productordermgmtName) {
        ProductOrderMgmt productordermgmt = repository.save(new ProductOrderMgmt(productordermgmtName));
        Optional<ProductOrderMgmt> oProductOrderMgmt = repository.findById(productordermgmt.getId());
        repository.delete(oProductOrderMgmt.get());
        Optional<ProductOrderMgmt> findProductOrderMgmt = repository.findById(oProductOrderMgmt.get().getId());
        assertFalse(findProductOrderMgmt.isPresent());
    }
}
