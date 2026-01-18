package com.example.demo.service.impl;

import com.example.demo.dto.*;
import com.example.demo.entity.Product;
import com.example.demo.exception.*;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

    public ProductServiceImpl(ProductRepository repo) {
        this.repo = repo;
    }

    @Override
    public ProductResponse create(ProductRequest request) {
        Product p = ProductMapper.toEntity(request);
        Product saved = repo.save(p);
        log.info("Product created: id={}, name={}, category={}",
                saved.getId(), saved.getName(), saved.getCategory());
        return ProductMapper.toResponse(saved);
    }

    @Override
    public List<ProductResponse> getAll() {
        return repo.findAll()
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    public ProductResponse getById(Long id) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return ProductMapper.toResponse(p);
    }

    @Override
    public ProductResponse update(Long id, ProductRequest request) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        p.setName(request.getName());
        p.setCategory(request.getCategory());
        p.setPrice(request.getPrice());
        p.setQuantity(request.getQuantity());

        Product saved = repo.save(p);
        return ProductMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        repo.delete(p);
    }

    @Override
    public ProductResponse increaseStock(Long id, int amount) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        p.setQuantity(p.getQuantity() + amount);
        Product saved = repo.save(p);
        log.info("Stock increased: id={}, +{}, newQty={}",
                id, amount, saved.getQuantity());
        return ProductMapper.toResponse(saved);
    }

    @Override
    public ProductResponse decreaseStock(Long id, int amount) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (p.getQuantity() - amount < 0) {
            throw new InvalidProductException(
                    "Cannot decrease stock below 0. currentQty=" + p.getQuantity()
            );
        }

        p.setQuantity(p.getQuantity() - amount);
        Product saved = repo.save(p);
        log.info("Stock decreased: id={}, -{}, newQty={}",
                id, amount, saved.getQuantity());
        return ProductMapper.toResponse(saved);
    }

    @Override
    public List<ProductResponse> getLowStock() {
        return repo.findAll()
                .stream()
                .filter(p -> p.getQuantity() < 5)
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> getByCategory(String category) {
        return repo.findAll()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    public InventoryValueResponse getTotalInventoryValue() {
        double total = repo.findAll()
                .stream()
                .mapToDouble(p -> p.getPrice() * p.getQuantity())
                .sum();
        return new InventoryValueResponse(total);
    }

    @Override
    public Page<ProductResponse> getAllPaged(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return repo.findAll(pageable).map(ProductMapper::toResponse);
    }


    @Override
    public List<ProductResponse> searchByCategory(String category) {
        return repo.findByCategoryIgnoreCase(category)
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> searchByName(String name) {
        return repo.findByNameContainingIgnoreCase(name)
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponse> searchByPriceRange(double minPrice, double maxPrice) {
        if (minPrice > maxPrice) {
            throw new InvalidProductException("minPrice cannot be greater than maxPrice");
        }
        return repo.findByPriceBetween(minPrice, maxPrice)
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
    }
}
