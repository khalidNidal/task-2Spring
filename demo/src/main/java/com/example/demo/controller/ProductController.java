package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.routes.ProductRoutes;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ProductRoutes.BASE)
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@Valid @RequestBody ProductRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        if (page != null && size != null) {
            Page<ProductResponseDTO> result = service.getAllPaged(page, size, sortBy, direction);
            return ResponseEntity.ok(result);
        }
        List<ProductResponseDTO> result = service.getAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping(ProductRoutes.ID)
    public ResponseEntity<ProductResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PutMapping(ProductRoutes.ID)
    public ResponseEntity<ProductResponseDTO> update(@PathVariable Long id,
                                                     @Valid @RequestBody ProductRequestDTO request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping(ProductRoutes.ID)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(ProductRoutes.INCREASE)
    public ResponseEntity<ProductResponseDTO> increase(@PathVariable Long id,
                                                       @Valid @RequestBody StockChangeRequestDTO req) {
        return ResponseEntity.ok(service.increaseStock(id, req.getAmount()));
    }

    @PostMapping(ProductRoutes.DECREASE)
    public ResponseEntity<ProductResponseDTO> decrease(@PathVariable Long id,
                                                       @Valid @RequestBody StockChangeRequestDTO req) {
        return ResponseEntity.ok(service.decreaseStock(id, req.getAmount()));
    }

    @GetMapping(ProductRoutes.LOW_STOCK)
    public ResponseEntity<List<ProductResponseDTO>> lowStock() {
        return ResponseEntity.ok(service.getLowStock());
    }

    @GetMapping(ProductRoutes.CATEGORY)
    public ResponseEntity<List<ProductResponseDTO>> byCategory(@PathVariable String category) {
        return ResponseEntity.ok(service.getByCategory(category));
    }

    @GetMapping(ProductRoutes.INVENTORY_VALUE)
    public ResponseEntity<InventoryValueResponseDTO> totalValue() {
        return ResponseEntity.ok(service.getTotalInventoryValue());
    }


    @GetMapping(ProductRoutes.SEARCH_CATEGORY)
    public ResponseEntity<List<ProductResponseDTO>> searchByCategory(@RequestParam String category) {
        return ResponseEntity.ok(service.searchByCategory(category));
    }

    @GetMapping(ProductRoutes.SEARCH_NAME)
    public ResponseEntity<List<ProductResponseDTO>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(service.searchByName(name));
    }

    @GetMapping(ProductRoutes.SEARCH_PRICE_RANGE)
    public ResponseEntity<List<ProductResponseDTO>> searchByPriceRange(
            @RequestParam double min,
            @RequestParam double max
    ) {
        return ResponseEntity.ok(service.searchByPriceRange(min, max));
    }
}
