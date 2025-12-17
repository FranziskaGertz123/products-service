package com.example.products.api.dto;

import com.example.products.application.dao.Unit;

import java.math.BigDecimal;


public record IngredientDto(
        String name,
        BigDecimal usage,
        Unit unit,
        BigDecimal price
) {}