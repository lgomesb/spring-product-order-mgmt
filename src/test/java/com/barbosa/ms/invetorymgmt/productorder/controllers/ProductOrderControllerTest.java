package com.barbosa.ms.invetorymgmt.productorder.controllers;

import com.barbosa.ms.invetorymgmt.productorder.ProductOrderApplicationTests;
import com.barbosa.ms.invetorymgmt.productorder.controller.ProductOrderController;
import com.barbosa.ms.invetorymgmt.productorder.domain.dto.OrderItemDTO;
import com.barbosa.ms.invetorymgmt.productorder.domain.dto.ProductOrderRequestDTO;
import com.barbosa.ms.invetorymgmt.productorder.domain.dto.ProductOrderResponseDTO;
import com.barbosa.ms.invetorymgmt.productorder.domain.dto.StatusProductOrderEnum;
import com.barbosa.ms.invetorymgmt.productorder.domain.records.OrderItemRecord;
import com.barbosa.ms.invetorymgmt.productorder.domain.records.ProductOrderRecord;
import com.barbosa.ms.invetorymgmt.productorder.services.ProductOrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles(value = "test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = {ProductOrderApplicationTests.class}, webEnvironment = WebEnvironment.DEFINED_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class ProductOrderControllerTest {

    private static UUID PRODUCT_ORDER_UUID = UUID.randomUUID();

    @Value("${server.servlet.context-path}/")
    private String STATIC_URI;

    @LocalServerPort
    private int port;

    @MockBean
    private ProductOrderService service;

    @InjectMocks
    private ProductOrderController controller;

    private ProductOrderRecord productorderRecord;

    private final Given given = new Given();
    private final When when = new When();
    private final Then then = new Then();


    @BeforeEach
    void setup() {
        productorderRecord = ProductOrderRecord.builder()
                .id(PRODUCT_ORDER_UUID)
                .status(StatusProductOrderEnum.DRAFT.name())
                .description("test-01")
                .items(Collections.singleton(new OrderItemRecord(UUID.randomUUID(), 1)))
                .build();

    }

    @Test
    void shouldSucceededWhenCallCreate() throws Exception {
        String productOrderRequestJSON = given.productOrderRequestJSON();
        given.productOrderCreationService();
        Response response = when.postProductOrder(productOrderRequestJSON);
        then.productOrderCreationResponseIsValid(response);
    }

    @Test
    void shouldSucceededWhenCallFindById() {
        given.productOrderFound();
        Response response = when.findProductOrder();
        then.productOrderFoundResponseIsValid(response);
    }

    @Test
    void shouldSucceededWhenCallUpdate() throws Exception {
        String productOrderUpdateRequestJSON = given.productOrderUpdateRequestJSON();
        Response response = when.updateProductOrder(productOrderUpdateRequestJSON);
        then.productOrderUpdatedResponseIsValid(response);
    }

    @Test
    void shouldSucceededWhenCallDelete() {
        Response response = when.deleteProductOrder();
        then.productOrderDeletedResponseIsValid(response);
    }

    @Test
    void shouldSucceededWhenCallListAll() {
        given.productOrderListed();
        Response response = when.findAllProductOrder();
        then.productOrderFindAllResponseIsValid(response);
    }

    @Test
    @DisplayName("List pageable Product Orders")
    void shouldSucceededWhenCallSearch() {
        given.productOrderSearched();
        Response response = when.searchProductOrder();
        then.productOrderSearchedResponseIsValid(response);
    }

    private class Given {
        public void productOrderCreationService() {
            doReturn(productorderRecord)
                    .when(service)
                        .create(any(ProductOrderRecord.class));
        }

        public String productOrderRequestJSON() throws JsonProcessingException {
            ProductOrderRequestDTO request = ProductOrderRequestDTO.builder()
                    .description("test-01")
                    .items(Collections.singletonList(new OrderItemDTO(UUID.randomUUID(), 1)))
                    .build();

            return new ObjectMapper().writeValueAsString(request);
        }

        public void productOrderFound() {
            doReturn(productorderRecord)
                    .when(service)
                            .findById(any(UUID.class));
        }

        public String productOrderUpdateRequestJSON() throws JsonProcessingException {
            ProductOrderRequestDTO request = ProductOrderRequestDTO.builder()
                    .description("test-01")
                    .items(Collections.singletonList(new OrderItemDTO(UUID.randomUUID(), 1)))
                    .build();

            return new ObjectMapper().writeValueAsString(request);
        }

        public void productOrderListed() {
            List<ProductOrderRecord> retVal = Collections.singletonList(new ProductOrderRecord(
                    PRODUCT_ORDER_UUID,
                    "product_order_test",
                    StatusProductOrderEnum.DRAFT.name(),
                    Collections.singleton(new OrderItemRecord(UUID.randomUUID(), 1))));

            doReturn(retVal)
                    .when(service)
                            .listAll();
        }

        public void productOrderSearched() {
            OrderItemRecord item = new OrderItemRecord(UUID.randomUUID(), 3);
            Set<OrderItemRecord> items = Collections.singleton(item);
            ProductOrderRecord productOrderRecord = ProductOrderRecord.builder()
                    .id(UUID.randomUUID())
                    .description("Test-Product-Order")
                    .status(StatusProductOrderEnum.DRAFT.name())
                    .items(items)
                    .build();

            when(service.search(anyString(), any(PageRequest.class)))
                    .thenReturn(new PageImpl<>(Collections.singletonList(productOrderRecord)));
        }
    }

    private class When {
        public Response postProductOrder(String productOrderRequestJSON) {
            return given()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .body(productOrderRequestJSON)
                    .log().all()
                    .when()
                    .post(STATIC_URI)
                    .then()
                    .assertThat()
                    .statusCode(HttpStatus.CREATED.value())
                    .extract()
                    .response();
        }

        public Response findProductOrder() {
            return given()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .pathParam("id", PRODUCT_ORDER_UUID.toString())
                    .when()
                    .get(STATIC_URI + "{id}")
                    .then()
                    .log().all()
                    .assertThat()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .response();
        }

        public Response updateProductOrder(String productOrderUpdateRequestJSON) {
            return given()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .pathParam("id", PRODUCT_ORDER_UUID.toString())
                    .body(productOrderUpdateRequestJSON)
                    .log().all()
                    .when()
                    .put(STATIC_URI + "{id}");
        }

        public Response deleteProductOrder() {
            return given()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .pathParam("id", PRODUCT_ORDER_UUID.toString())
                    .log().all()
                    .when()
                    .delete(STATIC_URI + "{id}");
        }

        public Response findAllProductOrder() {
            return given()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .log().all()
                    .when()
                    .get(STATIC_URI + "all");
        }

        public Response searchProductOrder() {
            return given()
                    .port(port)
                    .contentType(ContentType.JSON)
                    .log().all()
                    .when()
                    .get(STATIC_URI);
        }
    }

    private class Then {
        public void productOrderCreationResponseIsValid(Response response) {
            assertNotNull(response);
            assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
            ProductOrderResponseDTO productOrderResponseDTO = response.getBody().as(ProductOrderResponseDTO.class);
            assertNotNull(productOrderResponseDTO);
            assertNotNull(productOrderResponseDTO.getId());

        }

        public void productOrderFoundResponseIsValid(Response response) {
            assertNotNull(response);
            assertNotNull(response.getBody().jsonPath().getString("id"));
        }

        public void productOrderUpdatedResponseIsValid(Response response) {
            assertNotNull(response);
            assertEquals(response.getStatusCode(), HttpStatus.ACCEPTED.value());
        }

        public void productOrderDeletedResponseIsValid(Response response) {
            assertNotNull(response);
            assertEquals(response.getStatusCode(), HttpStatus.NO_CONTENT.value());
        }

        public void productOrderFindAllResponseIsValid(Response response) {
            assertNotNull(response);
            assertEquals(response.getStatusCode(), HttpStatus.OK.value());
            assertInstanceOf(ArrayList.class, response.getBody().jsonPath().get());
        }

        public void productOrderSearchedResponseIsValid(Response response) {
            assertNotNull(response);
            assertEquals(response.getStatusCode(), HttpStatus.PARTIAL_CONTENT.value());
            assertEquals("1", response.getBody().jsonPath().getString("totalPages"));
            assertEquals("1", response.getBody().jsonPath().getString("totalElements"));
            assertInstanceOf(ArrayList.class, response.getBody().jsonPath().get("content"));
            assertEquals("Test-Product-Order", response.getBody().jsonPath().getString("content[0].description"));
            assertInstanceOf(ArrayList.class, response.getBody().jsonPath().get("content[0].items"));
            assertEquals("3", response.getBody().jsonPath().getString("content[0].items[0].quantity"));
        }

        private static void checkProductId(Response response) {
            String idProductOrder = response.getHeader("Location");
            idProductOrder = idProductOrder.substring(idProductOrder.lastIndexOf("/")+1);
            assertNotNull(idProductOrder);
            assertFalse(idProductOrder.isEmpty());
            PRODUCT_ORDER_UUID = UUID.fromString(idProductOrder);
        }
    }
}
