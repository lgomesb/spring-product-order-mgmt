package com.barbosa.ms.productordermgmt.productordermgmt.controller;

import com.barbosa.ms.productordermgmt.productordermgmt.domain.dto.ProductOrderMgmtRequestDTO;
import com.barbosa.ms.productordermgmt.productordermgmt.domain.dto.ProductOrderMgmtResponseDTO;
import com.barbosa.ms.productordermgmt.productordermgmt.domain.records.ProductOrderMgmtRecord;
import com.barbosa.ms.productordermgmt.productordermgmt.services.ProductOrderMgmtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "ProductOrderMgmt", description = "Endpoints for ProductOrderMgmt operations")
@RestController
@RequestMapping("/")
public class ProductOrderMgmtController {

    @Autowired
    private ProductOrderMgmtService service;

    @Operation(summary = "Create ProductOrderMgmt", description = "Create a new ProductOrderMgmt", tags = { "ProductOrderMgmt" })
    @PostMapping
    public ResponseEntity<ProductOrderMgmtResponseDTO> create(@RequestBody @Valid ProductOrderMgmtRequestDTO dto) {

        ProductOrderMgmtRecord productordermgmtRecord = service.create(new ProductOrderMgmtRecord(null, dto.getName()));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(productordermgmtRecord.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Find ProductOrderMgmt by Id", description = "Find ProductOrderMgmt by id", tags = { "ProductOrderMgmt" })
    @GetMapping("{id}")
    public ResponseEntity<ProductOrderMgmtResponseDTO> findById(@PathVariable("id") String id) {
        ProductOrderMgmtRecord productordermgmtRecord = service.findById(UUID.fromString(id));
        return ResponseEntity.ok().body(ProductOrderMgmtResponseDTO.builder()
                .id(productordermgmtRecord.id())
                .name(productordermgmtRecord.name())
                .build());
    }

    @Operation(summary = "Update ProductOrderMgmt by Id", description = "Update ProductOrderMgmt by id", tags = { "ProductOrderMgmt" })
    @PutMapping("{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String id, @RequestBody ProductOrderMgmtRequestDTO dto) {
        service.update(new ProductOrderMgmtRecord(UUID.fromString(id), dto.getName()));
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Delete ProductOrderMgmt by Id", description = "Delete ProductOrderMgmt by id", tags = { "ProductOrderMgmt" })
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        service.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "List all ProductOrderMgmt", description = "List all ProductOrderMgmt in the database", tags = {"ProductOrderMgmt"})
    @GetMapping()
    public ResponseEntity<List<ProductOrderMgmtResponseDTO>> listAll() {
        List<ProductOrderMgmtResponseDTO> objects = service.listAll()
                .stream()
                .map(ProductOrderMgmtResponseDTO::create)
                .toList();

        return ResponseEntity.ok(objects);
    }

}
