package com.barbosa.ms.productordermgmt.productordermgmt.services.failed;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import com.barbosa.ms.productordermgmt.productordermgmt.domain.entities.ProductOrderMgmt;
import com.barbosa.ms.productordermgmt.productordermgmt.domain.records.ProductOrderMgmtRecord;
import com.barbosa.ms.productordermgmt.productordermgmt.repositories.ProductOrderMgmtRepository;
import com.barbosa.ms.productordermgmt.productordermgmt.services.impl.ProductOrderMgmtServiceImpl;


class ProductOrderMgmtServiceFailedTest {

    @InjectMocks
    private ProductOrderMgmtServiceImpl service;

    @Mock
    private ProductOrderMgmtRepository repository;
    
    private ProductOrderMgmt productordermgmt;
    private ProductOrderMgmtRecord productordermgmtRecord;
    private Given given = new Given();
    private When when = new When();
    private Then then = new Then();


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFailWhenCreate() {
        given.productordermgmtInicietedForFailueReturn();
        given.productordermgmtRecordInicietedForFailueReturn();
        when.saveProductOrderMgmtEntity();
        then.shouldBeFailueWhenCreateProductOrderMgmt(DataIntegrityViolationException.class);
    }

    @Test
    void shouldFailWhenFindById() {
        given.productordermgmtInicietedForFailueReturn();
        when.findProductOrderMgmtByIdWithFail();        
        then.shouldBeFailueWhenFindProductOrderMgmtById(ObjectNotFoundException.class);
    }

    @Test
    void shouldFailWhenUpdateWithIdNonExistent() {
        given.productordermgmtInicietedForFailueReturn();
        given.productordermgmtRecordInicietedForFailueReturn();
        when.findProductOrderMgmtByIdWithFail();        
        then.shouldBeFailueWhenFindProductOrderMgmtById(ObjectNotFoundException.class);    
    }

    @Test
    void shouldFailWhenUpdateWithInvalidArgument() {
        given.productordermgmtInicietedForFailueReturn();
        given.productordermgmtRecordInicietedForFailueReturn();
        when.findProductOrderMgmtById();
        when.saveProductOrderMgmtEntity();
        then.shouldBeFailueWhenUpdateProductOrderMgmt(DataIntegrityViolationException.class);        
    }

    @Test
    void shouldFailWhenDelete() {
        given.productordermgmtInicietedForFailueReturn();
        when.findProductOrderMgmtByIdWithFail();
        then.shouldBeFailueWhenDeleteProductOrderMgmt(ObjectNotFoundException.class);     
    }

    class Given {

        public UUID creationIdOfProductOrderMgmt() {
            return UUID.randomUUID();
        }

        void productordermgmtInicietedForFailueReturn() {
           productordermgmt = ProductOrderMgmt.builder()
                        .id(creationIdOfProductOrderMgmt())
                        .name(null)
                        .build();
        }

        void productordermgmtRecordInicietedForFailueReturn () {
            productordermgmtRecord = new ProductOrderMgmtRecord(productordermgmt.getId(), null);
        }
    }

    class When {
        
        public ProductOrderMgmtRecord callCreateInProductOrderMgmtSerivce() {
            return service.create(productordermgmtRecord);
        }
        
        public ProductOrderMgmtRecord callProductOrderMgmtServiceFindById() {
            return service.findById(given.creationIdOfProductOrderMgmt());
        }

        void callProductOrderMgmtSerivceUpdate() {
            service.update(productordermgmtRecord);
        }

        void callDelteInProductOrderMgmtSerivce() {
            service.delete(given.creationIdOfProductOrderMgmt());
        }

        void saveProductOrderMgmtEntity() {            
            doThrow(new DataIntegrityViolationException("Error inserting productordermgmt"))
                .when(repository)
                .save(any(ProductOrderMgmt.class));
        }

        void findProductOrderMgmtById() {
            when(repository.findById(any(UUID.class))).thenReturn(Optional.of(productordermgmt));
        }

        void deleteProductOrderMgmtEntity() {
            doNothing().when(repository).delete(any(ProductOrderMgmt.class));
        }

        void findProductOrderMgmtByIdWithFail() {
            doThrow(new ObjectNotFoundException("ProductOrderMgmt", given.creationIdOfProductOrderMgmt()))
                .when(repository).findById(any(UUID.class));
                
        }

    }
    
    class Then {

        public <T extends Throwable> void shouldBeFailueWhenCreateProductOrderMgmt(Class<T> clazz) {
           assertThrows(clazz, () -> {
                when.callCreateInProductOrderMgmtSerivce();
           });
        }

        
        public <T extends Throwable> void shouldBeFailueWhenFindProductOrderMgmtById(Class<T> clazz) {
            assertThrows(clazz, () -> {
                when.callProductOrderMgmtServiceFindById();
            });
        }
        
        public <T extends Throwable> void shouldBeFailueWhenUpdateProductOrderMgmt(Class<T> clazz) {
            assertThrows(clazz, () -> {
                when.callProductOrderMgmtSerivceUpdate();
            });
        }

        public <T extends Throwable> void shouldBeFailueWhenDeleteProductOrderMgmt(Class<T> clazz) {
            assertThrows(clazz, () -> {
                when.callDelteInProductOrderMgmtSerivce();
            });
        }
    }
}
