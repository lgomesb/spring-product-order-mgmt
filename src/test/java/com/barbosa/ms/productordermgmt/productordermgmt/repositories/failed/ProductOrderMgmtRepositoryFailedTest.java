package com.barbosa.ms.productordermgmt.productordermgmt.repositories.failed;

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
import com.barbosa.ms.productordermgmt.productordermgmt.ProductOrderMgmtApplicationTests;
import com.barbosa.ms.productordermgmt.productordermgmt.domain.entities.ProductOrderMgmt;
import com.barbosa.ms.productordermgmt.productordermgmt.repositories.ProductOrderMgmtRepository;

import jakarta.validation.ConstraintViolationException;

@ActiveProfiles(value = "test")
@DataJpaTest()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(classes = ProductOrderMgmtApplicationTests.class)
@TestInstance(Lifecycle.PER_CLASS)
class ProductOrderMgmtRepositoryFailedTest {

    @Autowired
    private ProductOrderMgmtRepository repository;

    private static Stream<Arguments> provideProductOrderMgmtData() {        
        return Stream.of(
          Arguments.of("ProductOrderMgmt-Test-01"),
          Arguments.of("ProductOrderMgmt-Test-02")
        );
    }

    
    @BeforeAll
    void shouldSuccessfulInjectComponent() {
        assertNotNull(repository);
    }

    @Order(0)
    @Test()
    @DisplayName("Should return Exception when ProductOrderMgmt not null")
    void shouldFailWhenCallCreate() {
        assertThrows(ConstraintViolationException.class, () -> {
            repository.saveAndFlush(new ProductOrderMgmt(null));
        }, "Should return Error when ProductOrderMgmt not null");
    }


    @Order(1)
    @ParameterizedTest
    @MethodSource("provideProductOrderMgmtData")
    void shouldFailWhenCallFindById(String productordermgmtName) {
        repository.save(new ProductOrderMgmt(productordermgmtName));
        Optional<ProductOrderMgmt> oProductOrderMgmt = repository.findById(UUID.randomUUID());
        assertThrows( ObjectNotFoundException.class, () -> {
            oProductOrderMgmt.orElseThrow(() ->
                 new ObjectNotFoundException("ProductOrderMgmt", UUID.randomUUID()));
        });
    }

  
    @Order(2)
    @ParameterizedTest
    @MethodSource("provideProductOrderMgmtData")
    void shouldFailWhenCallUpdate(String productordermgmtName) {
        String productordermgmtNameUpdate = "";
        ProductOrderMgmt productordermgmt = repository.save(new ProductOrderMgmt(productordermgmtName));
        Optional<ProductOrderMgmt> oProductOrderMgmt = repository.findById(productordermgmt.getId());        
        assertThrows(ConstraintViolationException.class, () -> {
            ProductOrderMgmt newProductOrderMgmt = oProductOrderMgmt.get();
            newProductOrderMgmt.setName(productordermgmtNameUpdate);
            repository.saveAndFlush(newProductOrderMgmt);
        }, "Should return Error when ProductOrderMgmt not blank or empty");
    }
  
    @Order(3)
    @ParameterizedTest
    @MethodSource("provideProductOrderMgmtData")
    void shouldFailWhenCallDelete(String productordermgmtName) {
        ProductOrderMgmt productordermgmt = new ProductOrderMgmt(UUID.randomUUID(), productordermgmtName);
        Optional<ProductOrderMgmt> oProductOrderMgmt = repository.findById(productordermgmt.getId());
        assertThrows( InvalidDataAccessApiUsageException.class, () -> {
            repository.delete(oProductOrderMgmt.orElse(null));
        }, "Should return Error when ProductOrderMgmt not blank or empty");
    }
}
