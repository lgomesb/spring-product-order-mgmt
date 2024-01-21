package com.barbosa.ms.productordermgmt.productordermgmt.services.succeed;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.barbosa.ms.productordermgmt.productordermgmt.domain.entities.ProductOrderMgmt;
import com.barbosa.ms.productordermgmt.productordermgmt.domain.records.ProductOrderMgmtRecord;
import com.barbosa.ms.productordermgmt.productordermgmt.repositories.ProductOrderMgmtRepository;
import com.barbosa.ms.productordermgmt.productordermgmt.services.impl.ProductOrderMgmtServiceImpl;


class ProductOrderMgmtServiceSucceedTest {

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
    void shouldSuccessWhenCreate() {
        given.productordermgmtInicietedForSuccessfulReturn();
        given.productordermgmtRecordInicietedForSuccessfulReturn();
        when.saveProductOrderMgmtEntity();
        ProductOrderMgmtRecord record = when.callCreateInProductOrderMgmtSerivce();
        then.shouldBeSuccessfulValidationRules(record);
    }

    @Test
    void shouldSuccessWhenFindById() {
        given.productordermgmtInicietedForSuccessfulReturn();
        when.findProductOrderMgmtById();
        ProductOrderMgmtRecord record = when.callProductOrderMgmtServiceFindById();
        then.shouldBeSuccessfulValidationRules(record);
    }

    @Test
    void shouldSuccessWhenUpdate() {
        given.productordermgmtInicietedForSuccessfulReturn();
        given.productordermgmtRecordInicietedForSuccessfulReturn();
        when.findProductOrderMgmtById();
        when.callProductOrderMgmtServiceFindById();
        when.saveProductOrderMgmtEntity();
        when.callProductOrderMgmtSerivceUpdate();
        then.shouldBeSuccessfulArgumentValidationByUpdate();        
    }

    @Test
    void shouldSuccessWhenDelete() {
        given.productordermgmtInicietedForSuccessfulReturn();
        when.findProductOrderMgmtById();
        when.deleteProductOrderMgmtEntity();
        when.callDelteInProductOrderMgmtSerivce();    
        then.shouldBeSuccessfulArgumentValidationByDelete();    
    }

    @Test
    void shouldSuccessWhenListAll() {
        given.productordermgmtInicietedForSuccessfulReturn();
        when.findAllProductOrderMgmt();
        List<ProductOrderMgmtRecord>  productordermgmtRecords = when.callListAllInProductOrderMgmtService();
        then.shouldBeSuccessfulArgumentValidationByListAll(productordermgmtRecords);
    }

    class Given {

        public UUID creationIdOfProductOrderMgmt() {
            return UUID.randomUUID();
        }

        void productordermgmtInicietedForSuccessfulReturn() {
           productordermgmt = ProductOrderMgmt.builder()
                        .id(creationIdOfProductOrderMgmt())
                        .name("ProductOrderMgmt-Test-Success")
                        .build();
        }

        void productordermgmtRecordInicietedForSuccessfulReturn () {
            productordermgmtRecord = new ProductOrderMgmtRecord(productordermgmt.getId(), productordermgmt.getName());
        }
    }

    class When {

        void saveProductOrderMgmtEntity() {
            when(repository.save(any(ProductOrderMgmt.class)))
            .thenReturn(productordermgmt);
        }

        void callProductOrderMgmtSerivceUpdate() {
            service.update(productordermgmtRecord);
        }

        void callDelteInProductOrderMgmtSerivce() {
            service.delete(given.creationIdOfProductOrderMgmt());
        }

        void deleteProductOrderMgmtEntity() {
            doNothing().when(repository).delete(any(ProductOrderMgmt.class));
        }

        public ProductOrderMgmtRecord callProductOrderMgmtServiceFindById() {
            return service.findById(given.creationIdOfProductOrderMgmt());
        }

        void findProductOrderMgmtById() {
            when(repository.findById(any(UUID.class))).thenReturn(Optional.of(productordermgmt));
        }

        public ProductOrderMgmtRecord callCreateInProductOrderMgmtSerivce() {
            return service.create(productordermgmtRecord);
        }

        void findAllProductOrderMgmt() {
            when(repository.findAll()).thenReturn(Collections.singletonList(productordermgmt));
        }

        public List<ProductOrderMgmtRecord> callListAllInProductOrderMgmtService() {
            return service.listAll();
        }
    }
    
    class Then {

        void shouldBeSuccessfulValidationRules(ProductOrderMgmtRecord record) {
            assertNotNull(record);
            assertNotNull(record.name());
            assertEquals(record.name(), productordermgmt.getName());
            assertNotNull(record.id());
            assertEquals(record.id(), productordermgmt.getId());
        }

        void shouldBeSuccessfulArgumentValidationByDelete() {
            ArgumentCaptor<ProductOrderMgmt> productordermgmtCaptor = ArgumentCaptor.forClass(ProductOrderMgmt.class);
            verify(repository).delete(productordermgmtCaptor.capture());
            assertNotNull(productordermgmtCaptor.getValue());
            assertEquals(productordermgmtCaptor.getValue().getName(),productordermgmt.getName());
        }

        void shouldBeSuccessfulArgumentValidationByUpdate() {
            ArgumentCaptor<ProductOrderMgmt> productordermgmtCaptor = ArgumentCaptor.forClass(ProductOrderMgmt.class);
            verify(repository).save(productordermgmtCaptor.capture());
            assertNotNull(productordermgmtCaptor.getValue());
            assertEquals(productordermgmtCaptor.getValue().getName(),productordermgmt.getName());
        }

        void shouldBeSuccessfulArgumentValidationByListAll(List<ProductOrderMgmtRecord> productordermgmtRecords) {
            assertNotNull(productordermgmtRecords);
            assertFalse(productordermgmtRecords.isEmpty());
        }
    }
}
