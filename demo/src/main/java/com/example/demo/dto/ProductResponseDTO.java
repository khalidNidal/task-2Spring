package com.example.demo.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {

    private Long id;
    private String name;
    private String category;
    private double price;
    private int quantity;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
