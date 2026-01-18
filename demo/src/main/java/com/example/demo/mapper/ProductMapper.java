package com.example.demo.mapper;

import com.example.demo.dto.ProductRequest;
import com.example.demo.dto.ProductResponse;
import com.example.demo.entity.Product;

public class ProductMapper {

    public static Product toEntity(ProductRequest req) {
        return new Product(
                req.getName(),
                req.getCategory(),
                req.getPrice(),
                req.getQuantity()
        );
    }

    public static ProductResponse toResponse(Product p) {
        return new ProductResponse(
                p.getId(),
                p.getName(),
                p.getCategory(),
                p.getPrice(),
                p.getQuantity(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
}
