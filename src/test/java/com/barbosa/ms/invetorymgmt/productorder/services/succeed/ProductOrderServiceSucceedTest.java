package com.barbosa.ms.invetorymgmt.productorder.services.succeed;

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

import com.barbosa.ms.invetorymgmt.productorder.domain.entities.ProductOrder;
import com.barbosa.ms.invetorymgmt.productorder.domain.records.ProductOrderRecord;
import com.barbosa.ms.invetorymgmt.productorder.repositories.ProductOrderRepository;
import com.barbosa.ms.invetorymgmt.productorder.services.impl.ProductOrderServiceImpl;


class ProductOrderServiceSucceedTest {

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
    void shouldSuccessWhenCreate() {
        given.productorderInicietedForSuccessfulReturn();
        given.productorderRecordInicietedForSuccessfulReturn();
        when.saveProductOrderEntity();
        ProductOrderRecord record = when.callCreateInProductOrderSerivce();
        then.shouldBeSuccessfulValidationRules(record);
    }

    @Test
    void shouldSuccessWhenFindById() {
        given.productorderInicietedForSuccessfulReturn();
        when.findProductOrderById();
        ProductOrderRecord record = when.callProductOrderServiceFindById();
        then.shouldBeSuccessfulValidationRules(record);
    }

    @Test
    void shouldSuccessWhenUpdate() {
        given.productorderInicietedForSuccessfulReturn();
        given.productorderRecordInicietedForSuccessfulReturn();
        when.findProductOrderById();
        when.callProductOrderServiceFindById();
        when.saveProductOrderEntity();
        when.callProductOrderSerivceUpdate();
        then.shouldBeSuccessfulArgumentValidationByUpdate();        
    }

    @Test
    void shouldSuccessWhenDelete() {
        given.productorderInicietedForSuccessfulReturn();
        when.findProductOrderById();
        when.deleteProductOrderEntity();
        when.callDelteInProductOrderSerivce();    
        then.shouldBeSuccessfulArgumentValidationByDelete();    
    }

    @Test
    void shouldSuccessWhenListAll() {
        given.productorderInicietedForSuccessfulReturn();
        when.findAllProductOrder();
        List<ProductOrderRecord>  productorderRecords = when.callListAllInProductOrderService();
        then.shouldBeSuccessfulArgumentValidationByListAll(productorderRecords);
    }

    class Given {

        public UUID creationIdOfProductOrder() {
            return UUID.randomUUID();
        }

        void productorderInicietedForSuccessfulReturn() {
           productorder = ProductOrder.builder()
                        .id(creationIdOfProductOrder())
                        .status("ProductOrder-Test-Success")
                        .build();
        }

        void productorderRecordInicietedForSuccessfulReturn () {
            productorderRecord = new ProductOrderRecord(productorder.getId(), productorder.getStatus());
        }
    }

    class When {

        void saveProductOrderEntity() {
            when(repository.save(any(ProductOrder.class)))
            .thenReturn(productorder);
        }

        void callProductOrderSerivceUpdate() {
            service.update(productorderRecord);
        }

        void callDelteInProductOrderSerivce() {
            service.delete(given.creationIdOfProductOrder());
        }

        void deleteProductOrderEntity() {
            doNothing().when(repository).delete(any(ProductOrder.class));
        }

        public ProductOrderRecord callProductOrderServiceFindById() {
            return service.findById(given.creationIdOfProductOrder());
        }

        void findProductOrderById() {
            when(repository.findById(any(UUID.class))).thenReturn(Optional.of(productorder));
        }

        public ProductOrderRecord callCreateInProductOrderSerivce() {
            return service.create(productorderRecord);
        }

        void findAllProductOrder() {
            when(repository.findAll()).thenReturn(Collections.singletonList(productorder));
        }

        public List<ProductOrderRecord> callListAllInProductOrderService() {
            return service.listAll();
        }
    }
    
    class Then {

        void shouldBeSuccessfulValidationRules(ProductOrderRecord record) {
            assertNotNull(record);
            assertNotNull(record.name());
            assertEquals(record.name(), productorder.getStatus());
            assertNotNull(record.id());
            assertEquals(record.id(), productorder.getId());
        }

        void shouldBeSuccessfulArgumentValidationByDelete() {
            ArgumentCaptor<ProductOrder> productorderCaptor = ArgumentCaptor.forClass(ProductOrder.class);
            verify(repository).delete(productorderCaptor.capture());
            assertNotNull(productorderCaptor.getValue());
            assertEquals(productorderCaptor.getValue().getStatus(),productorder.getStatus());
        }

        void shouldBeSuccessfulArgumentValidationByUpdate() {
            ArgumentCaptor<ProductOrder> productorderCaptor = ArgumentCaptor.forClass(ProductOrder.class);
            verify(repository).save(productorderCaptor.capture());
            assertNotNull(productorderCaptor.getValue());
            assertEquals(productorderCaptor.getValue().getStatus(),productorder.getStatus());
        }

        void shouldBeSuccessfulArgumentValidationByListAll(List<ProductOrderRecord> productorderRecords) {
            assertNotNull(productorderRecords);
            assertFalse(productorderRecords.isEmpty());
        }
    }
}
