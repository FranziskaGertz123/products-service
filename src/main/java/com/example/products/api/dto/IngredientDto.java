package com.example.products.api.dto;

import java.math.BigDecimal;


public record IngredientDto(
        String name,
        BigDecimal usage,
        String unit,
        BigDecimal price
) {}