package com.barbosa.ms.invetorymgmt.productorder.controller;

import com.barbosa.ms.invetorymgmt.productorder.domain.dto.ProductOrderRequestDTO;
import com.barbosa.ms.invetorymgmt.productorder.domain.dto.ProductOrderResponseDTO;
import com.barbosa.ms.invetorymgmt.productorder.domain.dto.StatusProductOrderRequestDTO;
import com.barbosa.ms.invetorymgmt.productorder.domain.records.OrderItemRecord;
import com.barbosa.ms.invetorymgmt.productorder.domain.records.ProductOrderRecord;
import com.barbosa.ms.invetorymgmt.productorder.services.ProductOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Tag(name = "ProductOrder", description = "Endpoints for ProductOrder operations")
@RestController
@RequestMapping("/")
@CrossOrigin("http://localhost:4200")
public class ProductOrderController {

    private final ProductOrderService service;

    public ProductOrderController(ProductOrderService service) {
        this.service = service;
    }

    @Operation(summary = "Create ProductOrder", description = "Create a new ProductOrder", tags = { "ProductOrder" })
    @PostMapping
    public ResponseEntity<ProductOrderResponseDTO> create(@RequestBody @Valid ProductOrderRequestDTO dto) {

        Set<OrderItemRecord> items = dto.getItems()
                .stream()
                .map(i -> OrderItemRecord.builder()
                        .productId(i.getProductId())
                        .quantity(i.getQuantity())
                        .build())
                .collect(Collectors.toSet());

        ProductOrderRecord productorderRecord = service.create(ProductOrderRecord
                .builder()
                .description(dto.getDescription())
                .items(items)
                .build());

        ProductOrderResponseDTO response = ProductOrderResponseDTO.create(productorderRecord);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Find ProductOrder by Id", description = "Find ProductOrder by id", tags = { "ProductOrder" })
    @GetMapping("{id}")
    public ResponseEntity<ProductOrderResponseDTO> findById(@PathVariable("id") String id) {
        ProductOrderRecord productorderRecord = service.findById(UUID.fromString(id));
        return ResponseEntity.ok().body(ProductOrderResponseDTO.create(productorderRecord));
    }

    @Operation(summary = "Update ProductOrder by Id", description = "Update ProductOrder by id", tags = { "ProductOrder" })
    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody ProductOrderRequestDTO dto) {
        service.update(ProductOrderRecord.builder()
                        .id(UUID.fromString(id))
                        .items(dto.getItems().stream()
                                .map(i -> new OrderItemRecord(i.getProductId(), i.getQuantity()) )
                                .collect(Collectors.toSet()))
                        .description(dto.getDescription())
                .build());
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Update Status of ProductOrder", description = "Update Status of ProductOrder by id", tags = { "ProductOrder" })
    @PatchMapping("{id}")
    public ResponseEntity<Void> updateStatus(@PathVariable("id") String id, @RequestBody StatusProductOrderRequestDTO dto) {
        service.updateStatus(ProductOrderRecord.builder()
                        .id(UUID.fromString(id))
                        .status(dto.getStatus().name())
                .build());
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Delete ProductOrder by Id", description = "Delete ProductOrder by id", tags = { "ProductOrder" })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        service.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "ProductOrder list pageable", description = "List ProductOrder in the database", tags = {"ProductOrder"})
    @GetMapping()
    public ResponseEntity<Page<ProductOrderResponseDTO>> search(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "5") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {

        PageRequest pageRequest = PageRequest.of(page,
                linesPerPage,
                Sort.Direction.valueOf(direction.toUpperCase()),
                orderBy);

        Page<ProductOrderRecord> records = service.search(decodeParam(name), pageRequest);
        Page<ProductOrderResponseDTO> orders = records.map(ProductOrderResponseDTO::create);
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(orders);
    }

    private String decodeParam(String name) {
        return URLDecoder.decode(name, StandardCharsets.UTF_8);
    }

    @Operation(summary = "List all ProductOrder", description = "List all ProductOrder in the database", tags = {"ProductOrder"})
    @GetMapping("/all")
    public ResponseEntity<List<ProductOrderResponseDTO>> listAll() {
        List<ProductOrderResponseDTO> objects = service.listAll()
                .stream()
                .map(ProductOrderResponseDTO::create)
                .toList();

        return ResponseEntity.ok(objects);
    }

}
