package com.example.products.api.dto;

import java.math.BigDecimal;


public record IngredientDto(
        String name,
        BigDecimal amount,
        String unit
) {}