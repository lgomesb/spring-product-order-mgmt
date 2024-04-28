package com.barbosa.ms.invetorymgmt.productorder.controller;

import com.barbosa.ms.invetorymgmt.productorder.domain.dto.ProductOrderRequestDTO;
import com.barbosa.ms.invetorymgmt.productorder.domain.dto.ProductOrderResponseDTO;
import com.barbosa.ms.invetorymgmt.productorder.domain.records.in.ProductOrderRecordIn;
import com.barbosa.ms.invetorymgmt.productorder.services.ProductOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Tag(name = "ProductOrder", description = "Endpoints for ProductOrder operations")
@RestController
@RequestMapping("/")
public class ProductOrderController {

    private final ProductOrderService service;

    public ProductOrderController(ProductOrderService service) {
        this.service = service;
    }

    @Operation(summary = "Create ProductOrder", description = "Create a new ProductOrder", tags = { "ProductOrder" })
    @PostMapping
    public ResponseEntity<ProductOrderResponseDTO> create(@RequestBody @Valid ProductOrderRequestDTO dto) {

        ProductOrderRecordIn productorderRecordIn = service.create(ProductOrderRecordIn
                .builder()
                .description(dto.getDescription())
                .build());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(productorderRecordIn.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Find ProductOrder by Id", description = "Find ProductOrder by id", tags = { "ProductOrder" })
    @GetMapping("{id}")
    public ResponseEntity<ProductOrderResponseDTO> findById(@PathVariable("id") String id) {
        ProductOrderRecordIn productorderRecordIn = service.findById(UUID.fromString(id));
        return ResponseEntity.ok().body(ProductOrderResponseDTO.builder()
                .id(productorderRecordIn.id())
                .description(productorderRecordIn.description())
                .build());
    }

    @Operation(summary = "Update ProductOrder by Id", description = "Update ProductOrder by id", tags = { "ProductOrder" })
    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody ProductOrderRequestDTO dto) {
        service.update(ProductOrderRecordIn
                .builder()
                        .id(UUID.fromString(id))
                        .items(Collections.emptySet())
                        .description(dto.getDescription())
                .build());
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Delete ProductOrder by Id", description = "Delete ProductOrder by id", tags = { "ProductOrder" })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        service.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List all ProductOrder", description = "List all ProductOrder in the database", tags = {"ProductOrder"})
    @GetMapping()
    public ResponseEntity<List<ProductOrderResponseDTO>> listAll() {
        List<ProductOrderResponseDTO> objects = service.listAll()
                .stream()
                .map(ProductOrderResponseDTO::create)
                .toList();

        return ResponseEntity.ok(objects);
    }

}
