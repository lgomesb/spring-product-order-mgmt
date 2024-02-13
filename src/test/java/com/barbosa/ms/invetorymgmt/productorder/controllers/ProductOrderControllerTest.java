package com.barbosa.ms.invetorymgmt.productorder.controllers;

import com.barbosa.ms.invetorymgmt.productorder.ProductOrderApplicationTests;
import com.barbosa.ms.invetorymgmt.productorder.controller.ProductOrderController;
import com.barbosa.ms.invetorymgmt.productorder.domain.records.ProductOrderRecord;
import com.barbosa.ms.invetorymgmt.productorder.services.ProductOrderService;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;


@ActiveProfiles(value = "test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = {ProductOrderApplicationTests.class}, webEnvironment = WebEnvironment.DEFINED_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class ProductOrderControllerTest {

    private static UUID STATIC_UUID;
    private static final String STATIC_URI = "/product-order/";

    @LocalServerPort
    private int port;

    @MockBean
    private ProductOrderService service;

    @InjectMocks
    private ProductOrderController controller;

    private ProductOrderRecord productorderRecord;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        productorderRecord = ProductOrderRecord.builder()
                .id(UUID.randomUUID())
                .status("A")
                .build();

    }

    @Test
    @Order(0)
    void shouldSucceededWhenCallCreate() throws UnknownHostException {

        when(service.create(any(ProductOrderRecord.class))).thenReturn(productorderRecord);

        Response response = given()
            .port(port)
            .contentType(ContentType.JSON)
            .body("{\"status\": \""+ productorderRecord.status() +"\"}")
            .log().all()
            .when()
            .post(STATIC_URI)
            .then()
            .assertThat()
            .statusCode(HttpStatus.CREATED.value())
            .extract()
            .response();
            
        assertNotNull(response);
        String idProductOrder = response.getHeader("Location");
        idProductOrder = idProductOrder.substring(idProductOrder.lastIndexOf("/")+1);
        assertNotNull(idProductOrder);
        assertFalse(idProductOrder.isEmpty());
        STATIC_UUID = UUID.fromString(idProductOrder);

    }

    @Test
    @Order(1)
    void shouldSucceededWhenCallFindById() {
        when(service.findById(any(UUID.class))).thenReturn(productorderRecord);

        Response response = given()
            .port(port)
            .contentType(ContentType.JSON)
            .pathParam("id", STATIC_UUID.toString())
            .when()
            .get(STATIC_URI + "{id}")
            .then()
            .log().all()
            .assertThat()
            .statusCode(HttpStatus.OK.value())
            .extract()
            .response();

        assertNotNull(response);
        assertNotNull(response.getBody().jsonPath().getString("id"));

    }

    @Test
    @Order(2)
    void shouldSucceededWhenCallUpdate() {

        given()
            .port(port)
            .contentType(ContentType.JSON)
            .pathParam("id", STATIC_UUID.toString())
            .body("{\"name\": \"Teste-2\"}")
            .log().all()
            .when()
            .put(STATIC_URI + "{id}")
            .then()
            .assertThat()
            .statusCode(HttpStatus.ACCEPTED.value());

    }

    @Test
    @Order(3)
    void shouldSucceededWhenCallDelete() {

        given()
            .port(port)
            .contentType(ContentType.JSON)
            .pathParam("id", STATIC_UUID.toString())
            .log().all()
            .when()
            .delete(STATIC_URI + "{id}")
            .then()
            .assertThat()
            .statusCode(HttpStatus.NO_CONTENT.value());

    }

    @Test
    @Order(4)
    void shouldSucceededWhenCallListAll() {
        when(service.listAll()).thenReturn(
                Collections.singletonList(new ProductOrderRecord(
                        STATIC_UUID,
                        "A",
                        Collections.emptySet())));

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get(STATIC_URI)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());

    }

   
}
