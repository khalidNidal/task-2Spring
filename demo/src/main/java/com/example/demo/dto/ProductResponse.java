package com.example.demo.dto;

import lombok.*;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    private String name;
    private String category;
    private double price;
    private int quantity;

    private Instant createdAt;
    private Instant updatedAt;
}
