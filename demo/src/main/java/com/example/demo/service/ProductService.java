package com.example.demo.service;

import com.example.demo.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    ProductResponse create(ProductRequest request);
    List<ProductResponse> getAll();
    ProductResponse getById(Long id);
    ProductResponse update(Long id, ProductRequest request);
    void delete(Long id);

    ProductResponse increaseStock(Long id, int amount);
    ProductResponse decreaseStock(Long id, int amount);

    List<ProductResponse> getLowStock();
    List<ProductResponse> getByCategory(String category);
    InventoryValueResponse getTotalInventoryValue();

    Page<ProductResponse> getAllPaged(int page, int size, String sortBy, String direction);

    List<ProductResponse> searchByCategory(String category);
    List<ProductResponse> searchByName(String name);
    List<ProductResponse> searchByPriceRange(double minPrice, double maxPrice);
}
