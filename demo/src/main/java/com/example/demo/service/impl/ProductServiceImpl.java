package com.example.demo.service.impl;

import com.example.demo.dto.*;
import com.example.demo.entity.Product;
import com.example.demo.exception.*;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository repo, ProductMapper productMapper) {
        this.repo = repo;
        this.productMapper = productMapper;
    }

    @Override
    public ProductResponseDTO create(ProductRequestDTO request) {
        Product p = productMapper.toEntity(request);
        Product saved = repo.save(p);
        log.info("Product created: id={}, name={}, category={}",
                saved.getId(), saved.getName(), saved.getCategory());
        return productMapper.toResponse(saved);
    }

    @Override
    public List<ProductResponseDTO> getAll() {
        return productMapper.toResponseList(repo.findAll());
    }

    @Override
    public ProductResponseDTO getById(Long id) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toResponse(p);
    }

    @Override
    public ProductResponseDTO update(Long id, ProductRequestDTO request) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        p.setName(request.getName());
        p.setCategory(request.getCategory());
        p.setPrice(request.getPrice());
        p.setQuantity(request.getQuantity());

        Product saved = repo.save(p);
        return productMapper.toResponse(saved);
    }

    @Override
    public void delete(Long id) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        repo.delete(p);
    }

    @Override
    public ProductResponseDTO increaseStock(Long id, int amount) {
        Product p = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        p.setQuantity(p.getQuantity() + amount);
        Product saved = repo.save(p);
        log.info("Stock increased: id={}, +{}, newQty={}",
                id, amount, saved.getQuantity());
        return productMapper.toResponse(saved);
    }

    @Override
    public ProductResponseDTO decreaseStock(Long id, int amount) {
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
        return productMapper.toResponse(saved);
    }

    @Override
    public List<ProductResponseDTO> getLowStock() {
        return repo.findAll()
                .stream()
                .filter(p -> p.getQuantity() < 5)
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProductResponseDTO> getByCategory(String category) {
        return repo.findAll()
                .stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .map(productMapper::toResponse)
                .toList();
    }

    @Override
    public InventoryValueResponseDTO getTotalInventoryValue() {
        double total = repo.findAll()
                .stream()
                .mapToDouble(p -> p.getPrice() * p.getQuantity())
                .sum();
        return new InventoryValueResponseDTO(total);
    }

    @Override
    public Page<ProductResponseDTO> getAllPaged(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return repo.findAll(pageable).map(productMapper::toResponse);
    }


    @Override
    public List<ProductResponseDTO> searchByCategory(String category) {
        return productMapper.toResponseList(repo.findByCategoryIgnoreCase(category));
    }

    @Override
    public List<ProductResponseDTO> searchByName(String name) {
        return productMapper.toResponseList(repo.findByNameContainingIgnoreCase(name));
    }

    @Override
    public List<ProductResponseDTO> searchByPriceRange(double minPrice, double maxPrice) {
        if (minPrice > maxPrice) {
            throw new InvalidProductException("minPrice cannot be greater than maxPrice");
        }
        return productMapper.toResponseList(repo.findByPriceBetween(minPrice, maxPrice));
    }
}
