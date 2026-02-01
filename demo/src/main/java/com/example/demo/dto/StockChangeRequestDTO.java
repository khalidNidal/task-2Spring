package com.example.demo.dto;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockChangeRequestDTO {

    @Positive(message = "amount must be > 0")
    private int amount;
}
