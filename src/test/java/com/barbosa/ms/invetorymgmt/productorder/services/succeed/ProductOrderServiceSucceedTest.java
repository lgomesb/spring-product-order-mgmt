package com.barbosa.ms.invetorymgmt.productorder.services.succeed;

import com.barbosa.ms.invetorymgmt.productorder.domain.entities.OrderItem;
import com.barbosa.ms.invetorymgmt.productorder.domain.entities.ProductOrder;
import com.barbosa.ms.invetorymgmt.productorder.domain.records.OrderItemRecord;
import com.barbosa.ms.invetorymgmt.productorder.domain.records.in.ProductOrderRecordIn;
import com.barbosa.ms.invetorymgmt.productorder.repositories.ProductOrderRepository;
import com.barbosa.ms.invetorymgmt.productorder.services.impl.ProductOrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProductOrderServiceSucceedTest {

    @InjectMocks
    private ProductOrderServiceImpl service;

    @Mock
    private ProductOrderRepository repository;
    
    private ProductOrder productOrder;
    private Set<OrderItem> items;

    private ProductOrderRecordIn productOrderRecordIn;
    private final Given given = new Given();
    private final When when = new When();
    private final Then then = new Then();


    @Test
    void shouldSuccessWhenCreate() {
        given.productOrderInitiatedForSuccessfulReturn();
        given.productOrderRecordInitiatedForSuccessfulReturn();
        when.saveProductOrderEntity();
        ProductOrderRecordIn record = when.callCreateInProductOrderService();
        then.shouldBeSuccessGivenCreate()
                .and()
                .shouldBeSuccessfulValidationRules(record);
    }

    @Test
    void shouldSuccessWhenFindById() {
        given.productOrderInitiatedForSuccessfulReturn();
        when.findProductOrderById();
        ProductOrderRecordIn record = when.callProductOrderServiceFindById();
        then.shouldBeSuccessGivenFind()
                .and()
                .shouldBeSuccessfulValidationRules(record);
    }

    @Test
    void shouldSuccessWhenUpdate() {
        given.productOrderInitiatedForSuccessfulReturn();
        given.productOrderRecordInitiatedForSuccessfulReturn();
        when.findProductOrderById();
        when.callProductOrderServiceFindById();
        when.saveProductOrderEntity();
        when.callProductOrderServiceUpdate();
        then.shouldBeSuccessfulArgumentValidationByUpdate();
    }

    @Test
    void shouldSuccessWhenDelete() {
        given.productOrderInitiatedForSuccessfulReturn();
        when.findProductOrderById();
        when.deleteProductOrderEntity();
        when.callDeleteInProductOrderService();
        then.shouldBeSuccessfulArgumentValidationByDelete();
    }

    @Test
    void shouldSuccessWhenListAll() {
        given.productOrderInitiatedForSuccessfulReturn();
        when.findAllProductOrder();
        List<ProductOrderRecordIn> productOrderRecordIns = when.callListAllInProductOrderService();
        then.shouldBeSuccessfulArgumentValidationByListAll(productOrderRecordIns);
    }

    class Given {

        public UUID creationIdOfProductOrder() {
            return UUID.randomUUID();
        }

        void productOrderInitiatedForSuccessfulReturn() {
           productOrder = ProductOrder.builder()
                        .id(creationIdOfProductOrder())
                        .status("A")
                        .description("Test-01")
                        .build();
            this.orderItemsInitiatedForSuccessfulReturn();
        }

        void orderItemsInitiatedForSuccessfulReturn() {
            items = Collections.singleton(new OrderItem(
                    UUID.randomUUID(),
                    1,
                    productOrder
            ));
            productOrder.setItems(items);
        }

        void productOrderRecordInitiatedForSuccessfulReturn() {
            productOrderRecordIn = ProductOrderRecordIn
                    .builder()
                    .id(productOrder.getId())
                    .status(productOrder.getStatus())
                    .description("Test-01")
                    .items(items
                            .stream()
                            .map(i -> new OrderItemRecord(i.getProductId(), i.getQuantity()))
                            .collect(Collectors.toSet()))
                    .build();
            ;
        }
    }

    class When {

        void saveProductOrderEntity() {
            when(repository.save(any(ProductOrder.class)))
            .thenReturn(productOrder);
        }

        void callProductOrderServiceUpdate() {
            service.update(productOrderRecordIn);
        }

        void callDeleteInProductOrderService() {
            service.delete(given.creationIdOfProductOrder());
        }

        void deleteProductOrderEntity() {
            doNothing().when(repository).delete(any(ProductOrder.class));
        }

        public ProductOrderRecordIn callProductOrderServiceFindById() {
            return service.findById(given.creationIdOfProductOrder());
        }

        void findProductOrderById() {
            when(repository.findById(any(UUID.class))).thenReturn(Optional.of(productOrder));
        }

        public ProductOrderRecordIn callCreateInProductOrderService() {
            return service.create(productOrderRecordIn);
        }

        void findAllProductOrder() {
            when(repository.findAll()).thenReturn(Collections.singletonList(productOrder));
        }

        public List<ProductOrderRecordIn> callListAllInProductOrderService() {
            return service.listAll();
        }
    }
    
    class Then {

        class And extends Then {

            Then and() {
                return new Then();
            }
        }
        public void shouldBeSuccessfulValidationRules(ProductOrderRecordIn record) {
            assertNotNull(record);
            assertNotNull(record.description());
            assertEquals(record.description(), productOrder.getDescription());
            assertNotNull(record.id());
            assertEquals(record.id(), productOrder.getId());
        }
        void shouldBeSuccessfulArgumentValidationByDelete() {
            ArgumentCaptor<ProductOrder> productOrderCaptor = ArgumentCaptor.forClass(ProductOrder.class);
            verify(repository).delete(productOrderCaptor.capture());
            assertNotNull(productOrderCaptor.getValue());
            assertEquals(productOrderCaptor.getValue().getStatus(), productOrder.getStatus());
        }

        void shouldBeSuccessfulArgumentValidationByUpdate() {
            ArgumentCaptor<ProductOrder> productOrderCaptor = ArgumentCaptor.forClass(ProductOrder.class);
            verify(repository).save(productOrderCaptor.capture());
            assertNotNull(productOrderCaptor.getValue());
            assertEquals(productOrderCaptor.getValue().getStatus(), productOrder.getStatus());
        }

        void shouldBeSuccessfulArgumentValidationByListAll(List<ProductOrderRecordIn> productOrderRecordIns) {
            assertNotNull(productOrderRecordIns);
            assertFalse(productOrderRecordIns.isEmpty());
        }

        public And shouldBeSuccessGivenFind() {
            ArgumentCaptor<UUID> productOrderCaptor = ArgumentCaptor.forClass(UUID.class);
            verify(repository).findById(productOrderCaptor.capture());
            UUID productOrderId = productOrderCaptor.getValue();
            assertNotNull(productOrderId);
            return new And();
        }

        public And shouldBeSuccessGivenCreate() {
            ArgumentCaptor<ProductOrder> captor = ArgumentCaptor.forClass(ProductOrder.class);
            verify(repository).save(captor.capture());
            ProductOrder productOrderCaptor = captor.getValue();
            assertNotNull(productOrderCaptor);
            assertNotNull(productOrderCaptor.getDescription());
            assertNotNull(productOrderCaptor.getItems());
            assertFalse(productOrderCaptor.getItems().isEmpty());
            assertNotNull(productOrderCaptor.getItems().stream().findAny().get());
            return new And();
        }

    }

}
