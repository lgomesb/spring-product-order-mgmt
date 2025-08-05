package com.barbosa.ms.invetorymgmt.productorder.services.failed;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import com.barbosa.ms.invetorymgmt.productorder.domain.entities.ProductOrder;
import com.barbosa.ms.invetorymgmt.productorder.domain.records.ProductOrderRecord;
import com.barbosa.ms.invetorymgmt.productorder.repositories.ProductOrderRepository;
import com.barbosa.ms.invetorymgmt.productorder.services.impl.ProductOrderServiceImpl;


class ProductOrderServiceFailedTest {

    @InjectMocks
    private ProductOrderServiceImpl service;

    @Mock
    private ProductOrderRepository repository;
    
    private ProductOrder productorder;
    private ProductOrderRecord productorderRecord;
    private Given given = new Given();
    private When when = new When();
    private Then then = new Then();


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFailWhenCreate() {
        given.productorderInicietedForFailueReturn();
        given.productorderRecordInicietedForFailueReturn();
        when.saveProductOrderEntity();
        then.shouldBeFailueWhenCreateProductOrder(DataIntegrityViolationException.class);
    }

    @Test
    void shouldFailWhenFindById() {
        given.productorderInicietedForFailueReturn();
        when.findProductOrderByIdWithFail();        
        then.shouldBeFailueWhenFindProductOrderById(ObjectNotFoundException.class);
    }

    @Test
    void shouldFailWhenUpdateWithIdNonExistent() {
        given.productorderInicietedForFailueReturn();
        given.productorderRecordInicietedForFailueReturn();
        when.findProductOrderByIdWithFail();        
        then.shouldBeFailueWhenFindProductOrderById(ObjectNotFoundException.class);    
    }

    @Test
    void shouldFailWhenUpdateWithInvalidArgument() {
        given.productorderInicietedForFailueReturn();
        given.productorderRecordInicietedForFailueReturn();
        when.findProductOrderById();
        when.saveProductOrderEntity();
        then.shouldBeFailueWhenUpdateProductOrder(DataIntegrityViolationException.class);        
    }

    @Test
    void shouldFailWhenDelete() {
        given.productorderInicietedForFailueReturn();
        when.findProductOrderByIdWithFail();
        then.shouldBeFailueWhenDeleteProductOrder(ObjectNotFoundException.class);     
    }

    class Given {

        public UUID creationIdOfProductOrder() {
            return UUID.randomUUID();
        }

        void productorderInicietedForFailueReturn() {
           productorder = ProductOrder.builder()
                        .id(creationIdOfProductOrder())
                        .status(null)
                        .build();
        }

        void productorderRecordInicietedForFailueReturn () {
            productorderRecord = ProductOrderRecord
                    .builder()
                    .id(productorder.getId())
                    .description(null)
                    .items(Collections.emptySet())
                    .build();
        }
    }

    class When {
        
        public ProductOrderRecord callCreateInProductOrderSerivce() {
            return service.create(productorderRecord);
        }
        
        public ProductOrderRecord callProductOrderServiceFindById() {
            return service.findById(given.creationIdOfProductOrder());
        }

        void callProductOrderSerivceUpdate() {
            service.update(productorderRecord);
        }

        void callDelteInProductOrderSerivce() {
            service.delete(given.creationIdOfProductOrder());
        }

        void saveProductOrderEntity() {            
            doThrow(new DataIntegrityViolationException("Error inserting productorder"))
                .when(repository)
                .save(any(ProductOrder.class));
        }

        void findProductOrderById() {
            when(repository.findById(any(UUID.class))).thenReturn(Optional.of(productorder));
        }

        void deleteProductOrderEntity() {
            doNothing().when(repository).delete(any(ProductOrder.class));
        }

        void findProductOrderByIdWithFail() {
            doThrow(new ObjectNotFoundException("ProductOrder", given.creationIdOfProductOrder()))
                .when(repository).findById(any(UUID.class));
                
        }

    }
    
    class Then {

        public <T extends Throwable> void shouldBeFailueWhenCreateProductOrder(Class<T> clazz) {
           assertThrows(clazz, () -> {
                when.callCreateInProductOrderSerivce();
           });
        }

        
        public <T extends Throwable> void shouldBeFailueWhenFindProductOrderById(Class<T> clazz) {
            assertThrows(clazz, () -> {
                when.callProductOrderServiceFindById();
            });
        }
        
        public <T extends Throwable> void shouldBeFailueWhenUpdateProductOrder(Class<T> clazz) {
            assertThrows(clazz, () -> {
                when.callProductOrderSerivceUpdate();
            });
        }

        public <T extends Throwable> void shouldBeFailueWhenDeleteProductOrder(Class<T> clazz) {
            assertThrows(clazz, () -> {
                when.callDelteInProductOrderSerivce();
            });
        }
    }
}
