package com.example.demo.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String category;

    @Positive
    private double price;

    @PositiveOrZero
    private int quantity;
}
