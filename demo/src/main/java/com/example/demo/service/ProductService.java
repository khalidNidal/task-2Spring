package com.example.demo.service;

import com.example.demo.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    ProductResponseDTO create(ProductRequestDTO request);
    List<ProductResponseDTO> getAll();
    ProductResponseDTO getById(Long id);
    ProductResponseDTO update(Long id, ProductRequestDTO request);
    void delete(Long id);

    ProductResponseDTO increaseStock(Long id, int amount);
    ProductResponseDTO decreaseStock(Long id, int amount);

    List<ProductResponseDTO> getLowStock();
    List<ProductResponseDTO> getByCategory(String category);
    InventoryValueResponseDTO getTotalInventoryValue();

    Page<ProductResponseDTO> getAllPaged(int page, int size, String sortBy, String direction);

    List<ProductResponseDTO> searchByCategory(String category);
    List<ProductResponseDTO> searchByName(String name);
    List<ProductResponseDTO> searchByPriceRange(double minPrice, double maxPrice);
}
