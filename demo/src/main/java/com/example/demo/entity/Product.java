package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name must not be empty")
    private String name;

    @NotBlank(message = "category must not be empty")
    private String category;

    @Positive(message = "price must be > 0")
    private double price;

    @PositiveOrZero(message = "quantity must be >= 0")
    private int quantity;

    @CreatedDate
    @Column(updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    public Product(String name, String category, double price, int quantity) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }
}
