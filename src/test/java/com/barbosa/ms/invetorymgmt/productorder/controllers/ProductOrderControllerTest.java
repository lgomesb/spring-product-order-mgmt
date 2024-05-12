package com.barbosa.ms.invetorymgmt.productorder.controllers;

import com.barbosa.ms.invetorymgmt.productorder.ProductOrderApplicationTests;
import com.barbosa.ms.invetorymgmt.productorder.controller.ProductOrderController;
import com.barbosa.ms.invetorymgmt.productorder.domain.dto.OrderItemDTO;
import com.barbosa.ms.invetorymgmt.productorder.domain.dto.ProductOrderRequestDTO;
import com.barbosa.ms.invetorymgmt.productorder.domain.records.OrderItemRecord;
import com.barbosa.ms.invetorymgmt.productorder.domain.records.in.ProductOrderRecordIn;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

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

    private ProductOrderRecordIn productorderRecordIn;

    private final Given given = new Given();
    private final When when = new When();
    private final Then then = new Then();


    @BeforeEach
    void setup() {
        productorderRecordIn = ProductOrderRecordIn.builder()
                .id(PRODUCT_ORDER_UUID)
                .status("A")
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
        given.productOrderSearched();
        Response response = when.findProductOrder();
        then.productOrderSearchedResponseIsValid(response);
    }

    @Test
    void shouldSucceededWhenCallUpdate() throws Exception {
        String productOrderUpdateRequestJSON = given.productOrderUpdateRequestJSON();
        Response response = when.updateProductOrder(productOrderUpdateRequestJSON);
        then.productOrderUpdatedResponseIsValid(response);
    }

    @Test
    void shouldSucceededWhenCallDelete() {
        Response response = when.deleteProductOrtder();
        then.productOrderDeletedResponseIsValid(response);
    }

    @Test
    void shouldSucceededWhenCallListAll() {
        given.productOrderListed();
        Response response = when.findAllProductOrder();
        then.productOrderFindAllResponseIsValid(response);
    }


    private class Given {
        public void productOrderCreationService() {
            doReturn(productorderRecordIn)
                    .when(service)
                        .create(any(ProductOrderRecordIn.class));
        }

        public String productOrderRequestJSON() throws JsonProcessingException {
            ProductOrderRequestDTO request = ProductOrderRequestDTO.builder()
                    .description("test-01")
                    .items(Collections.singletonList(new OrderItemDTO(UUID.randomUUID(), 1)))
                    .build();

            return new ObjectMapper().writeValueAsString(request);
        }

        public void productOrderSearched() {
            doReturn(productorderRecordIn)
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
            List<ProductOrderRecordIn> retVal = Collections.singletonList(new ProductOrderRecordIn(
                    PRODUCT_ORDER_UUID,
                    "product_order_test",
                    "A",
                    Collections.singleton(new OrderItemRecord(UUID.randomUUID(), 1))));

            doReturn(retVal)
                    .when(service)
                            .listAll();
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

        public Response deleteProductOrtder() {
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
                    .get(STATIC_URI);
        }
    }

    private class Then {
        public void productOrderCreationResponseIsValid(Response response) {
            assertNotNull(response);
            String idProductOrder = response.getHeader("Location");
            idProductOrder = idProductOrder.substring(idProductOrder.lastIndexOf("/")+1);
            assertNotNull(idProductOrder);
            assertFalse(idProductOrder.isEmpty());
            PRODUCT_ORDER_UUID = UUID.fromString(idProductOrder);
        }

        public void productOrderSearchedResponseIsValid(Response response) {
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
    }
}
